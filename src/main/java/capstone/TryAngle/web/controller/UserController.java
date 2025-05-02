package capstone.TryAngle.web.controller;

import capstone.TryAngle.common.ApiResponse;
import capstone.TryAngle.common.status.SuccessStatus;
import capstone.TryAngle.service.AuthService;
import capstone.TryAngle.web.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<SuccessStatus> signup(@RequestBody UserRequestDTO.SignupRequestDTO request) {
        authService.signup(request);
        return ApiResponse.onSuccess(SuccessStatus.SIGNUP_SUCCESS);
    }

}
