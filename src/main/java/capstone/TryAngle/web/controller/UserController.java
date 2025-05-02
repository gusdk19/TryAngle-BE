package capstone.TryAngle.web.controller;

import capstone.TryAngle.common.ApiResponse;
import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.common.status.SuccessStatus;
import capstone.TryAngle.service.AuthService;
import capstone.TryAngle.web.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final AuthService authService;

    @PostMapping("/checkEmail")
    public ApiResponse<?> checkEmail(@RequestBody UserRequestDTO.EmailCheckDTO emailDto) {
        String email = emailDto.getEmail();
        if (email == null || email.isBlank()) {
            throw new GeneralException(ErrorStatus.MISSING_REQUIRED_VALUE);
        }
        if (!authService.isEmailAvailable(email)) {
            throw new GeneralException(ErrorStatus.DUPLICATE_EMAIL);
        }
        return ApiResponse.onSuccess(SuccessStatus.AVAILABLE_EMAIL, null);
    }

    @PostMapping("/checkNickname")
    public ApiResponse<?> checkNickname(@RequestBody UserRequestDTO.NicknameCheckDTO nicknameDto) {
        String nickname = nicknameDto.getNickname();
        if (nickname == null || nickname.isBlank()) {
            throw new GeneralException(ErrorStatus.MISSING_REQUIRED_VALUE);
        }
        if (!authService.isNicknameAvailable(nickname)) {
            throw new GeneralException(ErrorStatus.DUPLICATE_NICKNAME);
        }
        return ApiResponse.onSuccess(SuccessStatus.AVAILABLE_NICKNAME, null);
    }

    @PostMapping("/signup")
    public ApiResponse<SuccessStatus> signup(@RequestBody UserRequestDTO.SignupRequestDTO request) {
        if (Stream.of(request.getEmail(), request.getName(), request.getNickname(),
                request.getPhone(), request.getPassword())
                .anyMatch(v -> v == null || v.isBlank())) {
            throw new GeneralException(ErrorStatus.MISSING_REQUIRED_VALUE);
        }
        // 이메일 중복, 닉네임 중복은 앞선 과정에서 확인 후 전달
        authService.signup(request);
        return ApiResponse.onSuccess(SuccessStatus.SIGNUP_SUCCESS, null);
    }

}
