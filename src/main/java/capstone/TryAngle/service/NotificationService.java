package capstone.TryAngle.service;

import capstone.TryAngle.web.dto.NotificationRequestDTO;
import capstone.TryAngle.web.dto.NotificationResponseDTO;

import java.util.List;

public interface NotificationService {
    List<?> getNotifications(String email);
    void markAsRead(Integer notificationId);
    NotificationResponseDTO.ChallengeNotificationsDTO challengeInviteNotification(NotificationRequestDTO.InviteRequestDTO inviteDto);
}
