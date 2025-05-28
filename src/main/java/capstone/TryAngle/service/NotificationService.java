package capstone.TryAngle.service;

import capstone.TryAngle.web.dto.NotificationResponseDTO;

import java.util.List;

public interface NotificationService {
    List<NotificationResponseDTO.AllNotificationsDTO> getNotifications(String email);
}
