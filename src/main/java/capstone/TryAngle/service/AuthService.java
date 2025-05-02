package capstone.TryAngle.service;

import capstone.TryAngle.web.dto.UserRequestDTO;

public interface AuthService {
    boolean isEmailAvailable(String email);
    boolean isNicknameAvailable(String nickname);
    void signup(UserRequestDTO.SignupRequestDTO request);
}
