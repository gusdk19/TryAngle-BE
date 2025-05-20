package capstone.TryAngle.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserResponseDTO {

    @Getter
    @AllArgsConstructor
    public static class LoginResponseDTO {
        private String token;
    }

    @Getter
    @AllArgsConstructor
    public static class MypageDTO {
        private Integer userId;
        private String email;
        private String name;
        private String phone;
        private String description;
        private String badgeDescription;
        private String nickname;
        private String profileImage;
        private Integer challengeMoney;
        private Integer returnMoney;
        private Integer followers;
        private Integer followees;
    }

    @Getter
    @AllArgsConstructor
    public static class FollowingsDTO {
        private Integer userId;
        private String nickname;
        private String profileImage;
    }

    @Getter
    @AllArgsConstructor
    public static class FollowersDTO {
        private Integer userId;
        private String nickname;
        private String profileImage;
    }

    @Getter
    @AllArgsConstructor
    public static class AllUsersDTO {
        private Integer userId;
        private String nickname;
        private String profileImage;
        private Boolean isFollowing;
    }
}
