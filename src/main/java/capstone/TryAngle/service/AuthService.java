package capstone.TryAngle.service;

import capstone.TryAngle.web.dto.UserRequestDTO;

public interface AuthService {
    void signup(UserRequestDTO.SignupRequestDTO request);
}
