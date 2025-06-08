package capstone.TryAngle.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ParticipationResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class getParticipationDTO {

        @JsonProperty("challenge_id")
        private Integer challengeId;

        @JsonProperty("status")
        private Integer status;

        @JsonProperty("participation_success")
        private Boolean participationSuccess;

        @JsonProperty("deposit_amount")
        private Integer depositAmount;

        @JsonProperty("deposit_status")
        private Integer depositStatus;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        @JsonProperty("auth_status")
        private boolean authStatus;

        @JsonProperty("auth_vote_status")
        private boolean authVoteStatus;
    }


}
