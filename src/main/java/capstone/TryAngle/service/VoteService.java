package capstone.TryAngle.service;

import capstone.TryAngle.model.challenge.Auth;
import capstone.TryAngle.web.dto.ChallengeResponseDTO;
import capstone.TryAngle.web.dto.UserResponseDTO;

import java.util.List;

public interface VoteService {
    public void evaluateAuthSuccessAfterVoting(Auth auth);

    List<ChallengeResponseDTO.VoteStatusDTO> getMyVoteStatus(Integer challengeId, String email);
}
