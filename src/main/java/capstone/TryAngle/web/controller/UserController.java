package capstone.TryAngle.web.controller;

import capstone.TryAngle.common.ApiResponse;
import capstone.TryAngle.common.status.SuccessStatus;
import capstone.TryAngle.service.AuthService;
import capstone.TryAngle.service.UserService;
import capstone.TryAngle.web.dto.UserRequestDTO;
import capstone.TryAngle.web.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/checkEmail")
    public ApiResponse<?> checkEmail(@RequestBody UserRequestDTO.EmailCheckDTO emailDto) {
        authService.validateEmail(emailDto.getEmail());
        return ApiResponse.onSuccess(SuccessStatus.AVAILABLE_EMAIL, null);
    }

    @PostMapping("/checkNickname")
    public ApiResponse<?> checkNickname(@RequestBody UserRequestDTO.NicknameCheckDTO nicknameDto) {
        authService.validateNickname(nicknameDto.getNickname());
        return ApiResponse.onSuccess(SuccessStatus.AVAILABLE_NICKNAME, null);
    }

    @PostMapping("/signup")
    public ApiResponse<SuccessStatus> signup(@RequestBody UserRequestDTO.SignupRequestDTO request) {
        // 이메일 중복, 닉네임 중복은 앞선 과정에서 확인 후 전달
        authService.signup(request);
        return ApiResponse.onSuccess(SuccessStatus.SIGNUP_SUCCESS, null);
    }

    @PostMapping("/login")
    public ApiResponse<UserResponseDTO.LoginResponseDTO> login(@RequestBody UserRequestDTO.LoginRequestDTO request) {
        UserResponseDTO.LoginResponseDTO response = authService.login(request.getEmail(), request.getPassword());
        return ApiResponse.onSuccess(SuccessStatus.LOGIN_SUCCESS, response);
    }

    @PostMapping("/report")
    public ApiResponse<?> userReport(@RequestBody UserRequestDTO.ReportRequestDTO reportDTO,
                                     @AuthenticationPrincipal User user) {
        String email = user.getUsername();

        userService.report(email, reportDTO);
        return ApiResponse.onSuccess(SuccessStatus.REPORT_SUCCESS, null);
    }

}
