package capstone.TryAngle.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserResponseDTO {

    @Getter
    @AllArgsConstructor
    public static class LoginResponseDTO {
        private String token;
    }
}
