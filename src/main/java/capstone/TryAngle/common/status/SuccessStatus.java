package capstone.TryAngle.common.status;

import capstone.TryAngle.common.code.BaseCode;
import capstone.TryAngle.common.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    // 가장 일반적인 응답
    _OK(HttpStatus.OK, "COMMON200", "요청에 성공했습니다."),

    // User 관련 응답
    SIGNUP_SUCCESS(HttpStatus.OK, "USER200", "회원가입에 성공했습니다."),
    AVAILABLE_EMAIL(HttpStatus.OK, "USER200", "사용 가능한 이메일입니다."),
    AVAILABLE_NICKNAME(HttpStatus.OK, "USER200", "사용 가능한 닉네임입니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "USER200", "로그인에 성공했습니다."),
    MODIFY_SUCCESS(HttpStatus.OK, "USER200", "회원 정보 수정에 성공했습니다."),
    FOLLOW_SUCCESS(HttpStatus.OK, "USER200", "팔로우에 성공했습니다."),
    UNFOLLOW_SUCCESS(HttpStatus.OK, "USER200", "언팔로우에 성공했습니다."),
    REPORT_SUCCESS(HttpStatus.OK, "USER200", "신고가 완료되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}