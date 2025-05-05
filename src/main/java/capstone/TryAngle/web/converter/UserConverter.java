package capstone.TryAngle.web.converter;

import capstone.TryAngle.model.user.User;
import capstone.TryAngle.web.dto.UserRequestDTO;
import capstone.TryAngle.web.dto.UserResponseDTO;

public class UserConverter {

    public static User toUser(UserRequestDTO.SignupRequestDTO dto, String encodedPassword) {
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

    public static UserResponseDTO.MypageDTO toMyPage(User user, int followerCnt, int followeeCnt) {
        return new UserResponseDTO.MypageDTO(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getDescription(),
                user.getBadgeDescription(),
                user.getNickname(),
                user.getProfileImage(),
                user.getChallengeMoney(),
                user.getReturnMoney(),
                followerCnt,
                followeeCnt
        );
    }
}
