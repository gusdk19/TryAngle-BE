package capstone.TryAngle.web.controller;

import capstone.TryAngle.common.ApiResponse;
import capstone.TryAngle.common.status.SuccessStatus;
import capstone.TryAngle.service.BadgeService;
import capstone.TryAngle.web.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class BadgeController {

    private final BadgeService badgeService;

    @PostMapping("/getBadge")
    public ApiResponse<?> getBadge(@RequestBody UserRequestDTO.getBadgeDTO badgeDTO,
                                   @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        badgeService.getBadge(email, badgeDTO.getBadgeId());
        return ApiResponse.onSuccess(SuccessStatus.GET_BADGE_SUCCESS, null);
    }

    @GetMapping("/badges")
    public ApiResponse<?> userBadges(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        return ApiResponse.onSuccess(SuccessStatus._OK, badgeService.userBadges(email));
    }
}
