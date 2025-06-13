package capstone.TryAngle.web.dto;

import capstone.TryAngle.model.user.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class NotificationResponseDTO {

    @Getter
    @AllArgsConstructor
    public static class AllNotificationsDTO {
        private Integer notificationId;
        private NotificationType notificationType;
        private String receiverNickname;
        private String senderNickname;
        private Boolean isRead;
        private LocalDateTime createdAt;
        private String message;
    }

    @Getter
    @AllArgsConstructor
    public static class ChallengeNotificationsDTO {
        private Integer challengeId;
        private String challengeName;
        private String inviteCode;
        private String message;
    }
}
