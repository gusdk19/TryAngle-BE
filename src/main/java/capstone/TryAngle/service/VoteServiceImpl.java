package capstone.TryAngle.service;

import capstone.TryAngle.model.challenge.Auth;

public class VoteServiceImpl {

    // vote service -> vote create 후에 시행
    // voteRepository.save(vote);
    // evaluateAuthSuccessAfterVoting(vote.getAuth());
//    public void evaluateAuthSuccessAfterVoting(Auth auth) {
//        int approveCount = voteRepository.countByAuth_AuthenticationIdAndVoteTypeTrue(auth.getAuthenticationId());
//        int totalParticipants = auth.getParticipation().getChallenge().getNowPeople();
//        if (approveCount > totalParticipants / 2 && !Boolean.TRUE.equals(auth.getAuthSuccess())) {
//            auth.setAuthSuccess(true);
//            authRepository.save(auth);
//        }
//    }
}
