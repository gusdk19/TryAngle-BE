package capstone.TryAngle.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class UserRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupRequestDTO {
        private String email;
        private String name;
        private String nickname;
        private String phone;
        private String password;
        private String description;
        private String profileImage;
    }

    @Getter
    public static class EmailCheckDTO {
        private String email;
    }

    @Getter
    public static class NicknameCheckDTO {
        private String nickname;
    }

    @Getter
    public static class LoginRequestDTO {
        private String email;
        private String password;
    }

    @Getter
    public static class ModifyUserRequestDTO {
        private String nickname;
        private String profileImage;
        private String description;
    }

    @Getter
    public static class FollowDTO {
        private String nickname;
    }

    @Getter
    public static class ReportRequestDTO {
        private String targetNickname;
        private String reason;
    }

    @Getter
    public static class WithdrawalDTO {
        private Integer amount;
    }

    @Getter
    public static class getBadgeDTO {
        private Integer badgeId;
    }

    @Getter
    public static class FindIdDTO {
        private String name;
        private String phone;
    }
}
