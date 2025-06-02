package capstone.TryAngle.web.converter;

import capstone.TryAngle.model.user.Notification;
import capstone.TryAngle.web.dto.NotificationResponseDTO;

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
}
