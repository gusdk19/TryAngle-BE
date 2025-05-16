package capstone.TryAngle.web.dto;

import lombok.Builder;
import lombok.Getter;

public class UserRequestDTO {

    @Getter
    @Builder
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
    }
}
