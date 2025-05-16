package capstone.TryAngle.service;

import capstone.TryAngle.web.dto.UserRequestDTO;
import capstone.TryAngle.web.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO.MypageDTO getMypageByEmail(String email);
    void modifyUserInfo(String email, UserRequestDTO.ModifyUserRequestDTO userDto);
}
