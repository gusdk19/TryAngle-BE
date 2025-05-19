package capstone.TryAngle.web.controller;

import capstone.TryAngle.common.ApiResponse;
import capstone.TryAngle.common.status.SuccessStatus;
import capstone.TryAngle.service.UserService;
import capstone.TryAngle.web.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MypageController {
    private final UserService userService;

    @GetMapping("/mypage")
    public ApiResponse<?> getMypage(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        return ApiResponse.onSuccess(SuccessStatus._OK, userService.getMypageByEmail(email));
    }

    @PutMapping("/modify")
    public ApiResponse<?> modifyUserInfo(@RequestBody UserRequestDTO.ModifyUserRequestDTO modifyDto,
                                         @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        userService.modifyUserInfo(email, modifyDto);
        return ApiResponse.onSuccess(SuccessStatus.MODIFY_SUCCESS, null);
    }

    @PutMapping("/modify/description")
    public ApiResponse<?> modifyDescription(@RequestBody UserRequestDTO.ModifyUserRequestDTO modifyDTO,
                                            @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        userService.modifyDescription(email, modifyDTO);
        return ApiResponse.onSuccess(SuccessStatus.MODIFY_SUCCESS, null);
    }

    @GetMapping("/followings")
    public ApiResponse<?> getFollowings(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        return ApiResponse.onSuccess(SuccessStatus._OK, userService.getUserFollowings(email));
    }

    @GetMapping("/followers")
    public ApiResponse<?> getFollowers(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        return ApiResponse.onSuccess(SuccessStatus._OK, userService.getUserFollowers(email));
    }

}
