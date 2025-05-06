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
}
