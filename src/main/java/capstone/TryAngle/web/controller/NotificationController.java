package capstone.TryAngle.web.controller;

import capstone.TryAngle.common.ApiResponse;
import capstone.TryAngle.common.status.SuccessStatus;
import capstone.TryAngle.service.NotificationService;
import capstone.TryAngle.web.dto.NotificationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notification")
    public ApiResponse<?> getNotification(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        return ApiResponse.onSuccess(SuccessStatus._OK, notificationService.getNotifications(email));
    }

    @PatchMapping("/notification/{notificationId}/read")
    public ApiResponse<?> readNotification(@PathVariable("notificationId") Integer notificationId) {
        notificationService.markAsRead(notificationId);
        return ApiResponse.onSuccess(SuccessStatus.MARK_READ_SUCCESS, null);
    }

    @PostMapping("/invite/notification")
    public ApiResponse<?> inviteNotification(@RequestBody NotificationRequestDTO.InviteRequestDTO inviteDto) {
        return ApiResponse.onSuccess(SuccessStatus.CHALLENGE_NOTIFICATION_SUCCESS, notificationService.challengeInviteNotification(inviteDto));
    }
}
