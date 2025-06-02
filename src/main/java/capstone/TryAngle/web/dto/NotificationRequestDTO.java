package capstone.TryAngle.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NotificationRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InviteRequestDTO {
        private Integer senderId;
        private Integer receiverId;
        private Integer challengeId;
        private String inviteCode;
    }
}
