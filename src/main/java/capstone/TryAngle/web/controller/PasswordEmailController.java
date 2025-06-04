package capstone.TryAngle.web.controller;

import capstone.TryAngle.common.ApiResponse;
import capstone.TryAngle.common.status.SuccessStatus;
import capstone.TryAngle.service.PasswordEmailService;
import capstone.TryAngle.web.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class PasswordEmailController {

    private final PasswordEmailService passwordEmailService;

    @PostMapping("/resetPassword")
    public ApiResponse<String> resetPassword(@RequestBody UserRequestDTO.PasswordResetDTO passwordResetDTO) {
        passwordEmailService.validateEmail(passwordResetDTO.getEmail());

        // 임시 비밀번호 생성하고 이메일 전송
        String temporaryPassword = passwordEmailService.joinEmail(passwordResetDTO.getEmail());

        // 임시 비밀번호로 DB 업데이트
        passwordEmailService.updatePassword(temporaryPassword, passwordResetDTO.getEmail());

        return ApiResponse.onSuccess(SuccessStatus.RESET_PASSWORD, temporaryPassword);
    }

}
