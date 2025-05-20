package capstone.TryAngle.service;

import capstone.TryAngle.web.dto.UserRequestDTO;
import capstone.TryAngle.web.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO.MypageDTO getMypageByEmail(String email);
    void modifyUserInfo(String email, UserRequestDTO.ModifyUserRequestDTO userDto);
    void modifyDescription(String email, UserRequestDTO.ModifyUserRequestDTO userDto);
    List<UserResponseDTO.FollowingsDTO> getUserFollowings(String email);
    List<UserResponseDTO.FollowersDTO> getUserFollowers(String email);
    void follow(String email, String followeeNickname);
    void unfollow(String email, String followeeNickname);
    List<UserResponseDTO.AllUsersDTO> getAllUsers(String email);
    void report(String email, UserRequestDTO.ReportRequestDTO reportDTO);
}
