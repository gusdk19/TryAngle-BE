package capstone.TryAngle.web.converter;

import capstone.TryAngle.model.user.User;
import capstone.TryAngle.web.dto.UserRequestDTO;

public class UserConverter {

    public static User toUser(UserRequestDTO.SignupRequestDTO dto,  String encodedPassword) {
        return User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .phone(dto.getPhone())
                .password(encodedPassword)
                .description(dto.getDescription())
                .profileImage(dto.getProfileImage())
                .build();
    }
}
