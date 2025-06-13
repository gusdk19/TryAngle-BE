package capstone.TryAngle.web.converter;

import capstone.TryAngle.model.challenge.Challenge;
import capstone.TryAngle.model.user.Notification;
import capstone.TryAngle.web.dto.NotificationResponseDTO;

import java.util.Optional;

public class NotificationConverter {

    public static NotificationResponseDTO.AllNotificationsDTO toAllNotificationsDTO(Notification notification) {
        return new NotificationResponseDTO.AllNotificationsDTO(
                notification.getNotificationId(),
                notification.getNotificationType(),
                notification.getReceiver().getNickname(),
                notification.getSender().getNickname(),
                notification.getIsRead(),
                notification.getCreatedDate(),
                notification.getMessage()
        );
    }

    public static NotificationResponseDTO.AllChallengeNotificationsDTO toAllChallengeNotificationsDTO(Notification notification, Challenge challenge) {
        return new NotificationResponseDTO.AllChallengeNotificationsDTO(
                notification.getNotificationId(),
                notification.getNotificationType(),
                notification.getReceiver().getNickname(),
                notification.getSender().getNickname(),
                notification.getIsRead(),
                notification.getCreatedDate(),
                notification.getMessage(),
                challenge.getChallengeId(),
                challenge.getChallengeName(),
                challenge.getInviteCode()
        );
    }
}
