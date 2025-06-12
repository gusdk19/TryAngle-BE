package capstone.TryAngle.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AuthResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class getAuthDTO {
        @JsonProperty("auth_id")
        private Integer authId;
        @JsonProperty("challenge_id")
        private Integer challengeId;
        @JsonProperty("user_nickname")
        private String userNickname;

        private String comment;
        @JsonProperty("auth_image")
        private String authImage;
        @JsonProperty("vote_count")
        private Integer voteCount;
        @JsonProperty("auth_success")
        private Boolean authSuccess;
        private LocalDateTime createdAt;


    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class createAuthDTO {
        @JsonProperty("auth_id")
       private Integer authId;

    }

}
