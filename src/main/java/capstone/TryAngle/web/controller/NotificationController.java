package capstone.TryAngle.web.controller;

import capstone.TryAngle.common.ApiResponse;
import capstone.TryAngle.common.status.SuccessStatus;
import capstone.TryAngle.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
