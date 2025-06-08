package capstone.TryAngle.service;

import capstone.TryAngle.model.challenge.Auth;
import capstone.TryAngle.web.dto.UserResponseDTO;

public interface VoteService {
    public void evaluateAuthSuccessAfterVoting(Auth auth);
}
