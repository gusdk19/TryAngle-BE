package capstone.TryAngle.service;

import capstone.TryAngle.web.dto.UserRequestDTO;
import capstone.TryAngle.web.dto.UserResponseDTO;

public interface AuthService {
    boolean isEmailAvailable(String email);
    boolean isNicknameAvailable(String nickname);
    void signup(UserRequestDTO.SignupRequestDTO request);
    UserResponseDTO.LoginResponseDTO login(String email, String rawPassword);
}
