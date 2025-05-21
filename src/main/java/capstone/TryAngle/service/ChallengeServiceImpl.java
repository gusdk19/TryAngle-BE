package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.model.challenge.Authentication;
import capstone.TryAngle.model.challenge.Challenge;
import capstone.TryAngle.model.challenge.Participation;
import capstone.TryAngle.model.user.User;
import capstone.TryAngle.repository.*;
import capstone.TryAngle.web.converter.ChallengeConverter;
import capstone.TryAngle.web.converter.ParticipationConverter;
import capstone.TryAngle.web.dto.ChallengeResponseDTO;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.web.dto.ParticipationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;
    private final VoteRepository voteRepository;

    @Override
    public List<ChallengeResponseDTO.getChallengeDTO> getChallenges() {
        List<Challenge> challenges = challengeRepository.findAll();
        return ChallengeConverter.toChallengeListDTO(challenges);

    }

    @Override
    public ChallengeResponseDTO.getChallengeDTO getChallengeById(Integer challengeId) {
            Challenge challenge = challengeRepository.findById(challengeId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_NOT_FOUND));
            return ChallengeConverter.toChallengeDTO(challenge);
        }

    @Override
    public List<ChallengeResponseDTO.getChallengeDTO> getMyChallenges(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new GeneralException((ErrorStatus.USER_NOT_FOUND)));
        List<Participation> participations = participationRepository.findAllByUserUserId(user.getUserId());
        return participations.stream()
                .map(participation -> ChallengeConverter.toChallengeDTO(participation.getChallenge()))
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
        boolean authStatus = authenticationRepository.existsByParticipationParticipationIdAndCreatedAtBetween(
                participationId,
                startOfDay,
                endOfDay
        );

        // 오늘 인증 중 가장 최근 것의 인증 ID로 투표 수 확인
        List<Authentication> todayAuths = authenticationRepository.findByParticipationParticipationIdAndCreatedAtBetween(
                participationId,
                startOfDay,
                endOfDay
        );

        boolean authVoteStatus = false;
        if (!todayAuths.isEmpty()) {
            Authentication todayAuth = todayAuths.get(0); // 가장 최근 인증 하나 기준
            int voteCount = voteRepository.countByAuthenticationAuthenticationIdAndCreatedAtBetween(
                    todayAuth.getAuthenticationId(),
                    startOfDay,
                    endOfDay
            );
            authVoteStatus = (voteCount == challenge.getNowPeople() - 1);
        }

        return ParticipationResponseDTO.getParticipationDTO.builder()
                .challenge_id(challenge.getChallengeId())
                .status(participation.getStatus())
                .participationSuccess(participation.getParticipationSuccess())
                .depositAmount(participation.getDepositAmount())
                .depositStatus(participation.getDepositStatus())
                .createdAt(participation.getCreatedAt())
                .authStatus(authStatus)
                .auth_vote_status(authVoteStatus)
                .build();
    }


}


