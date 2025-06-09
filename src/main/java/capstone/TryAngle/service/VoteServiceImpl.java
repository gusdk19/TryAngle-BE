package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.model.challenge.Auth;
import capstone.TryAngle.model.challenge.Challenge;
import capstone.TryAngle.model.challenge.Participation;
import capstone.TryAngle.model.user.User;
import capstone.TryAngle.repository.*;
import capstone.TryAngle.web.dto.ChallengeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final AuthRepository authRepository;
    private final ParticipationRepository participationRepository;
    private  final UserRepository userRepository;
    private  final ChallengeRepository challengeRepository;

    @Override
    public void evaluateAuthSuccessAfterVoting(Auth auth) {
        int approveCount = voteRepository.countByAuthAuthenticationIdAndVoteType(auth.getAuthenticationId(), true);
        int totalParticipants = participationRepository.countByChallengeChallengeId(auth.getParticipation().getChallenge().getChallengeId());

        // 찬성이 과반수 이상이면 authSuccess
        if (approveCount >= totalParticipants / 2 && !Boolean.TRUE.equals(auth.getAuthSuccess())) {
            auth.setAuthSuccess(true);
            authRepository.save(auth);
        }
    }

    @Override
    public List<ChallengeResponseDTO.VoteStatusDTO> getMyVoteStatus(Integer challengeId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_NOT_FOUND));


        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = todayStart.plusDays(1);


        // 오늘 생성된 모든 인증 조회 (이 챌린지의 모든 참가자 중)
        List<Auth> todayAuths = authRepository.findByParticipationChallengeChallengeIdAndCreatedAtBetween(
                challengeId, todayStart, todayEnd
        );

        List<Participation> participants = participationRepository.findAllByChallengeChallengeId(challengeId);

        List<ChallengeResponseDTO.VoteStatusDTO> result = new ArrayList<>();

        for (Participation p : participants) {
            if (p.getUser().getUserId().equals(user.getUserId())) continue;
            boolean voted = false;

            for (Auth auth : todayAuths) {
                if (auth.getParticipation().getUser().getUserId().equals(p.getUser().getUserId())) {
                    // 내 투표가 존재하는지 확인
                    boolean hasVoted = voteRepository.existsByAuthAuthenticationIdAndUserUserId(
                            auth.getAuthenticationId(), user.getUserId()
                    );
                    if (hasVoted) {
                        voted = true;
                        break;
                    }
                }
            }

            result.add(ChallengeResponseDTO.VoteStatusDTO.builder()
                    .nickname(p.getUser().getNickname())
                    .voted(voted)
                    .build());
        }

        return result;

    }
}