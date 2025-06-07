package capstone.TryAngle.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class createAuthDTO {
        @JsonProperty("challenge_id")
        private Integer challengeId;
        private String comment;
        @JsonProperty("auth_image")
        private String authImage;

        public void setAuthImage(String image) {
            this.authImage = image;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class editAuthDTO {
        private String comment;
        @JsonProperty("auth_image")
        private String authImage;

        public void setAuthImage(String image) {
            this.authImage = image;
        }
    }
}
