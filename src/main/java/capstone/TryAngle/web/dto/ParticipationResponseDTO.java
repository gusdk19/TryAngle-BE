package capstone.TryAngle.web.dto;

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

        private Integer challenge_id;
        private Integer status;
        private Boolean participationSuccess;
        private Integer depositAmount;
        private Integer depositStatus;
        private LocalDateTime createdAt;

        private boolean authStatus;
        private boolean auth_vote_status;
    }


}
