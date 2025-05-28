package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.model.user.Notification;
import capstone.TryAngle.model.user.User;
import capstone.TryAngle.repository.NotificationRepository;
import capstone.TryAngle.repository.UserRepository;
import capstone.TryAngle.web.converter.NotificationConverter;
import capstone.TryAngle.web.dto.NotificationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    @Override
    public List<NotificationResponseDTO.AllNotificationsDTO> getNotifications(String email) {
        User receiver = findUserByEmail(email);

        List<Notification> notifications = notificationRepository.findAllByReceiver(receiver);

        return notifications.stream()
                .map(NotificationConverter::toAllNotificationsDTO)
                .collect(Collectors.toList());
    }
}
