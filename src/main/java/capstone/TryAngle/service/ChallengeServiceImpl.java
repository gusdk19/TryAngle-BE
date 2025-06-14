package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.model.challenge.Auth;
import capstone.TryAngle.model.challenge.Category;
import capstone.TryAngle.model.challenge.Challenge;
import capstone.TryAngle.model.challenge.Participation;
import capstone.TryAngle.model.user.User;
import capstone.TryAngle.repository.*;
import capstone.TryAngle.web.converter.ChallengeConverter;
import capstone.TryAngle.web.dto.AuthResponseDTO;
import capstone.TryAngle.web.dto.ChallengeRequestDTO;
import capstone.TryAngle.web.dto.ChallengeResponseDTO;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.web.dto.ParticipationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final VoteRepository voteRepository;

    @Override
    public List<ChallengeResponseDTO.getChallengeDTO> getChallenges() {
        List<Challenge> challenges = challengeRepository.findAll();
        Map<Integer, List<Integer>> challengeIdToParticipants = new HashMap<>();

        for (Challenge challenge : challenges) {
            List<Integer> ids = participationRepository
                    .findAllByChallengeChallengeId(challenge.getChallengeId())
                    .stream()
                    .map(p -> p.getUser().getUserId())
                    .collect(Collectors.toList());

            challengeIdToParticipants.put(challenge.getChallengeId(), ids);
        }

        return ChallengeConverter.toChallengeListDTO(challenges, challengeIdToParticipants);
    }

    @Override
    public ChallengeResponseDTO.getChallengeDTO getChallengeById(Integer challengeId) {
            Challenge challenge = challengeRepository.findById(challengeId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_NOT_FOUND));


        List<Integer> participantIds = participationRepository
                .findAllByChallengeChallengeId(challengeId)
                .stream()
                .map(p -> p.getUser().getUserId())
                .collect(Collectors.toList());
            return ChallengeConverter.toChallengeDTO(challenge, participantIds);
        }

    @Override
    public List<ChallengeResponseDTO.getMyChallengeDTO> getMyChallenges(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<Participation> participations = participationRepository.findAllByUserUserId(user.getUserId());

        return participations.stream()
                .map(participation -> {
                    Challenge challenge = participation.getChallenge();
                    boolean participationSuccess = participation.getParticipationSuccess();

                    return ChallengeConverter.toMyChallengeDTO(challenge, participationSuccess);
                })
                .collect(Collectors.toList());
    }


    @Override
    public ParticipationResponseDTO.getParticipationDTO getMyChallengeStatus(String email, Integer challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_NOT_FOUND));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Participation participation = participationRepository.findByUserUserIdAndChallengeChallengeId(user.getUserId(), challengeId);
        if (participation == null) {
            throw new GeneralException(ErrorStatus.PARTICIPATION_NOT_FOUND);
        }

        Integer participationId = participation.getParticipationId();
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        // 오늘 인증 여부
        boolean authStatus = authRepository.existsByParticipationParticipationIdAndCreatedAtBetween(
                participationId,
                startOfDay,
                endOfDay
        );

        // 오늘 인증 중 가장 최근 것의 인증 ID로 투표 수 확인
        List<Auth> todayAuths = authRepository.findByParticipationParticipationIdAndCreatedAtBetween(
                participationId,
                startOfDay,
                endOfDay
        );
        boolean authVoteStatus = false;
        if (!todayAuths.isEmpty()) {
            Auth todayAuth = todayAuths.get(0); // 가장 최근 인증 하나 기준
            int voteCount = voteRepository.countByAuthAuthenticationIdAndCreatedAtBetween(
                    todayAuth.getAuthenticationId(),
                    startOfDay,
                    endOfDay
            );
            authVoteStatus = (voteCount == challenge.getNowPeople() - 1);
        }

        return ParticipationResponseDTO.getParticipationDTO.builder()
                .challengeId(challenge.getChallengeId())
                .status(participation.getStatus())
                .participationSuccess(participation.getParticipationSuccess())
                .depositAmount(participation.getDepositAmount())
                .depositStatus(participation.getDepositStatus())
                .createdAt(participation.getCreatedAt())
                .authStatus(authStatus)
                .authVoteStatus(authVoteStatus)
                .build();
    }

    @Override
    @Transactional
    public void deleteChallenge(Integer challengeId, String email) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_NOT_FOUND));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 유저가 챌린지의 리더인지 확인
        if (!challenge.getLeader().getUserId().equals(user.getUserId())) {
            throw new GeneralException(ErrorStatus.NOT_LEADER);
        }

        // 챌린지 삭제
        challengeRepository.delete(challenge);
    }


    @Override
    public void createChallenge(ChallengeRequestDTO.createChallengeDTO createChallengeDTO, String email, Integer deposit) {

        User leader = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if (Boolean.FALSE.equals(createChallengeDTO.getChallengePublic())) {
            if (createChallengeDTO.getInviteCode() == null || createChallengeDTO.getInviteCode().isBlank()) {
                throw new GeneralException(ErrorStatus.INVITE_CODE_REQUIRED);
            }
        }



        // 최소 예치금 조건
        if (deposit < createChallengeDTO.getMinDeposit()) {
            throw new GeneralException(ErrorStatus.DEPOSIT_TOO_SMALL);
        }

        if (createChallengeDTO.getStartDate().isAfter(createChallengeDTO.getEndDate())){
            throw new GeneralException(ErrorStatus.WRONG_DATE);
        }


        Challenge challenge = new Challenge(
                null,
                Category.values()[createChallengeDTO.getCategory()],
                leader,
                createChallengeDTO.getChallengeName(),
                createChallengeDTO.getChallengeThumbnail(),
                createChallengeDTO.getChallengeShortintro(),
                createChallengeDTO.getChallengeDescription(),
                createChallengeDTO.getChallengePublic(),
                createChallengeDTO.getStartDate(),
                createChallengeDTO.getEndDate(),
                createChallengeDTO.getAuthTimeStart(),
                createChallengeDTO.getAuthTimeEnd(),
                createChallengeDTO.getMaxPeople(),
                1, // 리더 1명 참여로 시작
                createChallengeDTO.getMinDeposit(),
                createChallengeDTO.getReturnType(),
                createChallengeDTO.getAuthFrequency(),
                createChallengeDTO.getInviteCode(), // invite_code
                createChallengeDTO.getDepositManageMethod(),
                createChallengeDTO.getAuthMethod(),
                createChallengeDTO.getVoteMethod(),

                null // createdAt
        );

        challengeRepository.save(challenge);


        // 참여 객체 생성
        Participation participation = new Participation(
                leader,
                challenge,
                0, // (ready, progress, completed)
                false,
                deposit,
                2 // (refunded, donated, not_refunded_yet)

        );
        participationRepository.save(participation);

        // 유저 현재 예치금 추가
        Integer currentChallengeMoney  = leader.getChallengeMoney();
        leader.updateChallengeMoney(currentChallengeMoney+deposit);

    }

    @Override
    public void updateChallenge(Integer challengeId, ChallengeRequestDTO.createChallengeDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_NOT_FOUND));

        if (!challenge.getLeader().getUserId().equals(user.getUserId())) {
            throw new GeneralException(ErrorStatus.NOT_LEADER);
        }

        Category category = Category.values()[dto.getCategory()];

        challenge.updateChallenge(
                dto.getChallengeName(),
                dto.getChallengeThumbnail(),
                dto.getChallengeShortintro(),
                dto.getChallengeDescription(),
                category,
                dto.getChallengePublic(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getAuthTimeStart(),
                dto.getAuthTimeEnd(),
                dto.getMaxPeople(),
                dto.getMinDeposit(),
                dto.getReturnType(),
                dto.getAuthFrequency(),
                dto.getDepositManageMethod(),
                dto.getAuthMethod(),
                dto.getVoteMethod()
        );
    }

    @Override
    public void joinChallenge(Integer challengeId, Integer deposit,  String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_NOT_FOUND));

        if (!challenge.getStartDate().isAfter(LocalDate.now())) {
            throw new GeneralException(ErrorStatus.JOIN_TOO_LATE);
        }

//        // 비공개 챌린지라면 초대 코드
//        if (!challenge.getChallengePublic()) {
//            if (inviteCode == null || !inviteCode.equals(challenge.getInviteCode())) {
//                throw new GeneralException(ErrorStatus.INVALID_INVITE_CODE);
//            }
//        }

        // 중복 참여 방지
        boolean alreadyJoined = participationRepository.existsByUserUserIdAndChallengeChallengeId(
                user.getUserId(), challengeId
        );
        if (alreadyJoined) {
            throw new GeneralException(ErrorStatus.ALREADY_PARTICIPATED);
        }

        // 참여 인원 제한
        if (challenge.getNowPeople() != null && challenge.getNowPeople() >= challenge.getMaxPeople()) {
            throw new GeneralException(ErrorStatus.CHALLENGE_FULL);
        }


        // 최소 예치금 조건
        if (deposit < challenge.getMinDeposit()) {
            throw new GeneralException(ErrorStatus.DEPOSIT_TOO_SMALL);
        }

        // 참여 상태 결정
        int status = challenge.getStartDate().isAfter(LocalDate.now()) ? 0 : 1;


        // 참여 객체 생성
        Participation participation = new Participation(
                user,
                challenge,
                status, // (ready, progress, completed)
                false,
                deposit,
                2 // (refunded, donated, not_refunded_yet)

        );
        participationRepository.save(participation);

        // 현재 인원 수 증가
        Integer currentPeople = challenge.getNowPeople() != null ? challenge.getNowPeople() : 0;
        challenge.setNowPeople(currentPeople + 1);
        challengeRepository.save(challenge);

        // 유저 현재 예치금 추가
        Integer currentChallengeMoney  = user.getChallengeMoney();
        user.updateChallengeMoney(currentChallengeMoney+deposit);
    }

    @Override
    public void verifyInviteCode(Integer challengeId, String inviteCode, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_NOT_FOUND));

        if (!challenge.getChallengePublic()) {
            if (inviteCode == null || !inviteCode.equals(challenge.getInviteCode())) {
                throw new GeneralException(ErrorStatus.INVALID_INVITE_CODE);
            }
        }
    }


    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    public void autoUpdateChallengeStatus() {
        System.out.println("챌린지 상태 스케줄러");

        LocalDate today = LocalDate.now();

        // 오늘 시작하는 챌린지들
        List<Challenge> startingChallenges = challengeRepository.findAllByStartDate(today);

        for (Challenge challenge : startingChallenges) {
            List<Participation> participants = participationRepository.findAllByChallengeChallengeId(challenge.getChallengeId());
            for (Participation p : participants) {
                if (p.getStatus() == 0) { // ready → progress
                    p.setStatus(1);
                }
            }
        }

        // 오늘 기준으로 종료된 챌린지들의 참여자 상태도 완료로 갱신
        List<Challenge> endingChallenges = challengeRepository.findAllByEndDateBefore(today);
        for (Challenge challenge : endingChallenges) {
            List<Participation> participants = participationRepository.findAllByChallengeChallengeId(challenge.getChallengeId());
            for (Participation p : participants) {
                if (p.getStatus() != 2) {
                    p.setStatus(2);
                }
            }
        }
    }


    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    public void autoEndChallenges() {
        System.out.println("스케쥴링");
        List<Challenge> endingChallenges = challengeRepository
                .findAllByEndDate(LocalDate.now());

        for (Challenge challenge : endingChallenges) {
            endChallenge(challenge.getChallengeId());
        }
    }

    @Override
    public void endChallenge(Integer challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_NOT_FOUND));

        Boolean isDonation = challenge.getReturnType();  // true: 기부, false: 환급
        List<Participation> participations = participationRepository.findAllByChallengeChallengeId(challengeId);

        int alphaPool = 0;
        List<Participation> fullySuccessParticipants = new ArrayList<>();

        // 1. 참여자별 달성률 및 기본 환급 처리
        for (Participation participation : participations) {
            int expected = getExpectedCount(challenge, participation);
            long success = participation.getAuthList().stream()
                    .filter(Auth::getAuthSuccess)
                    .count();

            double rate = expected > 0 ? (double) success / expected : 0.0;
            System.out.println("성공률"+ expected + "suc" + success + "rate: "+ rate);
            int deposit = participation.getDepositAmount();
            int refundAmount = 0;

            if (rate >= 0.9) {
                participation.setSuccess();
                refundAmount = deposit;

                // 100% 성공한 사람 저장 (추후 α 분배 대상)
                if (rate == 1.0) {
                    fullySuccessParticipants.add(participation);
                }

            } else if (rate >= 0.5) {
                participation.setSuccess();
                refundAmount = (int) Math.round(deposit * rate);
            } else {
                refundAmount = 0;
            }

            // 2. 종료 처리
            participation.markAsCompleted();
            participation.returnDeposit(isDonation);

            // 3. 금액 처리 (기부 아닐 때만)
            if (!isDonation) {
                User user = participation.getUser();
                user.updateChallengeMoney(user.getChallengeMoney() - deposit);

                // 환급
                if (refundAmount > 0) {
                    user.updateReturnMoney(user.getReturnMoney() + refundAmount);
                }

                // α 풀 계산
                alphaPool += (deposit - refundAmount);
            }
        }

        // 4. α 정산 (기부 아님 + 100% 성공자 있음)
        if (!isDonation && alphaPool > 0 && !fullySuccessParticipants.isEmpty()) {
            int share = alphaPool / fullySuccessParticipants.size();
            for (Participation p : fullySuccessParticipants) {
                User user = p.getUser();
                user.updateReturnMoney(user.getReturnMoney() + share);
            }
        }
    }




    @Override
    public ChallengeResponseDTO.createInviteCodeDTO createInviteCode(String inviteCode, Integer challengeId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_NOT_FOUND));

        // 리더 아니면
        if (!challenge.getLeader().getUserId().equals(user.getUserId())){
            throw new GeneralException(ErrorStatus.NOT_LEADER);
        }

        // 공개 챌린지면
        if (Boolean.TRUE.equals(challenge.getChallengePublic())){
            throw new GeneralException(ErrorStatus.CHALLENGE_PUBLIC);
        }
        challenge.setInviteCode(inviteCode);
        return ChallengeResponseDTO.createInviteCodeDTO.builder().inviteCode(inviteCode)
                .build();
    }

    @Override
    public void quitChallenge(Integer challengeId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_NOT_FOUND));

        Participation participation = participationRepository
                .findByUserUserIdAndChallengeChallengeId(user.getUserId(), challengeId);

        if (participation == null) {
            throw new GeneralException(ErrorStatus.NOT_PARTICIPATING);
        }

        // 챌린지 리더는 탈퇴 불가
        if (challenge.getLeader().getUserId().equals(user.getUserId())) {
            throw new GeneralException(ErrorStatus.LEADER_CANNOT_CANCEL_PARTICIPATION);
        }

        // 챌린지 시작 전까지만 탈퇴 가능
        if (LocalDate.now().isAfter(challenge.getStartDate())) {
            throw new GeneralException(ErrorStatus.CHALLENGE_ALREADY_STARTED);
        }

        participationRepository.delete(participation);

        // nowPeople 수 감소
        challenge.setNowPeople(challenge.getNowPeople() - 1);
        // 유저 전체 예치금 업데이트
        user.updateChallengeMoney(user.getChallengeMoney()-participation.getDepositAmount());
    }

    @Override
    public List<ChallengeResponseDTO.ChallengeDepositStatusDTO> getMyDepositStatus(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<Participation> participations = participationRepository.findAllByUserUserId(user.getUserId());

        return participations.stream()
                .map(p -> {
                    Integer depositAmount = p.getDepositAmount() != null ? p.getDepositAmount() : 0;
                    Integer status = p.getDepositStatus();

                    int refundAmount = (status != null && status == 0) ? depositAmount : 0;

                    return ChallengeResponseDTO.ChallengeDepositStatusDTO.builder()
                            .challengeId(p.getChallenge().getChallengeId())
                            .challengeName(p.getChallenge().getChallengeName())
                            .challengeThumbnail(p.getChallenge().getChallengeThumbnail())
                            .deposit(depositAmount)
                            .status(convertStatusToString(status))
                            .depositDate(p.getCreatedAt())
                            .depositReturnDate(p.getDepositReturnDate())
                            .refundAmount(refundAmount)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public ChallengeResponseDTO.ChallengeSuccessRateDTO getMySuccessRate(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<Participation> userParticipations = participationRepository.findAllByUserUserId(user.getUserId());

        int totalExpected = 0;
        int totalSuccess = 0;
        Map<Category, Integer> expectedPerCategory = new HashMap<>();
        Map<Category, Integer> successPerCategory = new HashMap<>();
        for (Participation p : userParticipations) {
            Challenge challenge = p.getChallenge();
            int expected = getExpectedCount(challenge, p);
            long success = p.getAuthList().stream()
                    .filter(Auth::getAuthSuccess)
                    .count();

            totalExpected += expected;
            totalSuccess += Math.min((int) success, expected);

            expectedPerCategory.merge(challenge.getCategory(), expected, Integer::sum);
            successPerCategory.merge(challenge.getCategory(), Math.min((int) success, expected), Integer::sum);
        }

        double totalSuccessRate = (totalExpected > 0)
                ? Math.round((totalSuccess * 1000.0 / totalExpected)) / 10.0
                : 0.0;


        List<ChallengeResponseDTO.ChallengeSuccessRateDTO.CategorySuccessRateDTO> categoryRates = new ArrayList<>();
        for (Category category : expectedPerCategory.keySet()) {
            int expected = expectedPerCategory.get(category);
            int success = successPerCategory.getOrDefault(category, 0);

            double rate = (expected > 0)
                    ? Math.round((success * 1000.0 / expected)) / 10.0
                    : 0.0;

            categoryRates.add(ChallengeResponseDTO.ChallengeSuccessRateDTO.CategorySuccessRateDTO.builder()
                    .category(category.name())  // 필요시 한글로 매핑 가능
                    .successRate(rate)
                    .build());
        }

        return ChallengeResponseDTO.ChallengeSuccessRateDTO.builder()
                .totalSuccessRate(totalSuccessRate)
                .categorySuccessRate(categoryRates)
                .build();
    }

    @Override
    public AuthResponseDTO.AuthCalendarDTO getChallengeCalendar(String email,String ym) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));


        if (ym == null || ym.length() != 6) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        int year = Integer.parseInt(ym.substring(0, 4));
        int month = Integer.parseInt(ym.substring(4, 6));

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        List<Auth> auths = authRepository
                .findAllByUserAndAuthSuccessTrueAndCreatedAtBetween(
                        user.getUserId(),
                        start.atStartOfDay(),
                        end.plusDays(1).atStartOfDay()
                );

        List<String> authDates = auths.stream()
                .map(auth -> auth.getCreatedAt().toLocalDate().toString())
                .distinct()
                .collect(Collectors.toList());

        return AuthResponseDTO.AuthCalendarDTO.builder()
                .yearMonth(ym)
                .authDates(authDates)
                .build();
    }


    private String convertStatusToString    (Integer statusCode) {
        return switch (statusCode) {
            case 0 -> "refunded";
            case 1 -> "donated";
            case 2 -> "not_refunded_yet";
            default -> "알수없음";
        };
    }

    private int getExpectedCount(Challenge challenge, Participation participation) {
        LocalDate from = challenge.getStartDate();
        LocalDate to = challenge.getEndDate().isBefore(LocalDate.now()) ? challenge.getEndDate() : LocalDate.now();

        long days = from.datesUntil(to.plusDays(1)).count();
        double ratio = getWeeklyRatio(challenge.getAuthFrequency());

        System.out.println("From "+from+"to "+to+ "days "+ days+ "ratio "+ratio);
        return (int) Math.round(days * ratio);
    }

    private double getWeeklyRatio(String freq) {
        return switch (freq.trim()) {
            case "매일" -> 1.0;
            case "평일 매일" -> 5.0 / 7.0;
            case "주말 매일" -> 2.0 / 7.0;
            case "주 1일" -> 1.0 / 7.0;
            case "주 2일" -> 2.0 / 7.0;
            case "주 3일" -> 3.0 / 7.0;
            case "주 4일" -> 4.0 / 7.0;
            case "주 5일" -> 5.0 / 7.0;
            case "주 6일" -> 6.0 / 7.0;
            default -> 0.0;
        };
    }

}


