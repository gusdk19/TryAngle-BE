package capstone.TryAngle.service;

import capstone.TryAngle.web.dto.AuthRequestDTO;
import capstone.TryAngle.web.dto.AuthResponseDTO;
import capstone.TryAngle.web.dto.UserRequestDTO;
import capstone.TryAngle.web.dto.UserResponseDTO;

import java.util.List;

public interface AuthService {
    void validateEmail(String email);
    void validateNickname(String nickname);
    void signup(UserRequestDTO.SignupRequestDTO request);
    UserResponseDTO.LoginResponseDTO login(String email, String rawPassword);

    AuthResponseDTO.createAuthDTO createAuth(String email, AuthRequestDTO.createAuthDTO createAuthDTO);

    void editAuth(Integer authenticationId, String email, AuthRequestDTO.editAuthDTO editAuthDTO);

    AuthResponseDTO.getAuthDTO getAuthById(String email, Integer authenticationId);

    void deleteAuth(String email, Integer authenticationId);

    void voteAuth(String email, Integer authenticationId, AuthRequestDTO.voteAuthDTO voteAuthDTO);

    void reactionAuth(String email, Integer authenticationId, AuthRequestDTO.reactionAuthDTO reactionAuthDTO);

    List<AuthResponseDTO.getAuthDTO> getAllAuth(String email, Integer challengeId);

    AuthResponseDTO.getAuthDTO getMyAuth(String email, Integer challengeId);
}

