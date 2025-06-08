package capstone.TryAngle.service;

import capstone.TryAngle.model.challenge.Auth;
import capstone.TryAngle.repository.AuthRepository;
import capstone.TryAngle.repository.ParticipationRepository;
import capstone.TryAngle.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final AuthRepository authRepository;
    private final ParticipationRepository participationRepository;

    @Override
    public void evaluateAuthSuccessAfterVoting(Auth auth) {
        int approveCount = voteRepository.countByAuthAuthenticationIdAndVoteType(auth.getAuthenticationId(), true);
        int totalParticipants = participationRepository.countByChallengeChallengeId(auth.getParticipation().getChallenge().getChallengeId());

        if (approveCount > totalParticipants / 2 && !Boolean.TRUE.equals(auth.getAuthSuccess())) {
            auth.setAuthSuccess(true);
            authRepository.save(auth);
        }
    }
}
