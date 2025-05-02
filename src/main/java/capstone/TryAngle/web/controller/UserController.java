package capstone.TryAngle.web.controller;

import capstone.TryAngle.common.ApiResponse;
import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.common.status.SuccessStatus;
import capstone.TryAngle.service.AuthService;
import capstone.TryAngle.web.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final AuthService authService;

    @PostMapping("/checkEmail")
    public ApiResponse<SuccessStatus> checkEmail(@RequestBody UserRequestDTO.EmailCheckDTO emailDto) {
        if (!authService.isEmailAvailable(emailDto.getEmail())) {
            throw new GeneralException(ErrorStatus.DUPLICATE_EMAIL);
        }
        return ApiResponse.onSuccess(SuccessStatus.AVAILABLE_EMAIL);
    }

    @PostMapping("/checkNickname")
    public ApiResponse<SuccessStatus> checkNickname(@RequestBody UserRequestDTO.NicknameCheckDTO nicknameDto) {
        if (!authService.isNicknameAvailable(nicknameDto.getNickname())) {
            throw new GeneralException(ErrorStatus.DUPLICATE_NICKNAME);
        }
        return ApiResponse.onSuccess(SuccessStatus.AVAILABLE_NICKNAME);
    }

    @PostMapping("/signup")
    public ApiResponse<SuccessStatus> signup(@RequestBody UserRequestDTO.SignupRequestDTO request) {
        authService.signup(request);
        return ApiResponse.onSuccess(SuccessStatus.SIGNUP_SUCCESS);
    }

}
