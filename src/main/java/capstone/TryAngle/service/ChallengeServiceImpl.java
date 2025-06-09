package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.model.challenge.Auth;
import capstone.TryAngle.model.challenge.Category;
import capstone.TryAngle.model.challenge.Challenge;
import capstone.TryAngle.model.challenge.Participation;
import capstone.TryAngle.model.user.User;
import capstone.TryAngle.repository.*;
import capstone.TryAngle.web.converter.ChallengeConverter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
                null, // invite_code
                createChallengeDTO.getDepositManageMethod(),
                createChallengeDTO.getAuthMethod(),
                createChallengeDTO.getVoteMethod(),
                null // createdAt
        );

        challengeRepository.save(challenge);

        // 최소 예치금 조건
        if (deposit < createChallengeDTO.getMinDeposit()) {
            throw new GeneralException(ErrorStatus.DEPOSIT_TOO_SMALL);
        }

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
    public void joinChallenge(Integer challengeId, Integer deposit, String inviteCode, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_NOT_FOUND));

        // 비공개 챌린지라면 초대 코드
        if (!challenge.getChallengePublic()) {
            if (inviteCode == null || !inviteCode.equals(challenge.getInviteCode())) {
                throw new GeneralException(ErrorStatus.INVALID_INVITE_CODE);
            }
        }

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


    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    public void autoEndChallenges() {
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

        List<Participation> participations = participationRepository
                .findAllByChallengeChallengeId(challengeId);

        for (Participation participation : participations) {
            // 상태 종료로 변경
            participation.markAsCompleted();

            // 성공한 경우만 처리
            if (Boolean.TRUE.equals(participation.getParticipationSuccess())) {
                int depositAmount = participation.getDepositAmount();
                User participant = participation.getUser();

                // 입금 상태 처리 (donated / refunded)
                participation.returnDeposit(isDonation);

                if (!isDonation) {
                    // 환급인 경우만 돈 처리
                    participant.updateChallengeMoney(participant.getChallengeMoney() - depositAmount);
                    participant.updateReturnMoney(depositAmount); // returnMoney += depositAmount

                }
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
                    Integer status = p.getStatus();

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

    private String convertStatusToString    (Integer statusCode) {
        return switch (statusCode) {
            case 0 -> "refunded";
            case 1 -> "donated";
            case 2 -> "not_refunded_yet";
            default -> "알수없음";
        };
    }


}


