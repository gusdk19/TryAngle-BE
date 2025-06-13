package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.model.challenge.Challenge;
import capstone.TryAngle.model.user.Notification;
import capstone.TryAngle.model.user.NotificationType;
import capstone.TryAngle.model.user.User;
import capstone.TryAngle.repository.ChallengeRepository;
import capstone.TryAngle.repository.NotificationRepository;
import capstone.TryAngle.repository.UserRepository;
import capstone.TryAngle.web.converter.NotificationConverter;
import capstone.TryAngle.web.dto.NotificationRequestDTO;
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
    private final ChallengeRepository challengeRepository;

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

    @Override
    public void markAsRead(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(()->new GeneralException(ErrorStatus.NOTIFICATION_NOT_FOUND));

        if (!notification.getIsRead()) {
            notification.markIsRead(true);
        }
    }

    @Override
    public NotificationResponseDTO.ChallengeNotificationsDTO challengeInviteNotification(NotificationRequestDTO.InviteRequestDTO inviteDto) {
        User sender = userRepository.findById(Long.valueOf(inviteDto.getSenderId()))
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        User receiver = userRepository.findById(Long.valueOf(inviteDto.getReceiverId()))
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(inviteDto.getChallengeId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_NOT_FOUND));

        String message = String.format(
                "%s님이 %s 챌린지에 나를 초대했어요.\n참여 코드는 %s입니다.",
                sender.getNickname(),
                challenge.getChallengeName(),
                inviteDto.getInviteCode()
        );

        Notification notification = Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .notificationType(NotificationType.CHALLENGE_INVITE)
                .message(message)
                .challengeId(challenge.getChallengeId())
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        return new NotificationResponseDTO.ChallengeNotificationsDTO(
                challenge.getChallengeId(),
                challenge.getChallengeName(),
                challenge.getInviteCode(),
                message
        );
    }
}
