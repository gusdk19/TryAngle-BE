package capstone.TryAngle.service;

import capstone.TryAngle.web.dto.AuthRequestDTO;
import capstone.TryAngle.web.dto.AuthResponseDTO;
import capstone.TryAngle.web.dto.UserRequestDTO;
import capstone.TryAngle.web.dto.UserResponseDTO;

public interface AuthService {
    void validateEmail(String email);
    void validateNickname(String nickname);
    void signup(UserRequestDTO.SignupRequestDTO request);
    UserResponseDTO.LoginResponseDTO login(String email, String rawPassword);

    void createAuth(String email, AuthRequestDTO.createAuthDTO createAuthDTO);

    void editAuth(Integer authenticationId, String email, AuthRequestDTO.editAuthDTO editAuthDTO);

    AuthResponseDTO.getAuthDTO getAuthById(String email, Integer authenticationId);

    void deleteAuth(String email, Integer authenticationId);
}
