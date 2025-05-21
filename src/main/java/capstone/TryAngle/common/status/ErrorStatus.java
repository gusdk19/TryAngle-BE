package capstone.TryAngle.common.status;

import capstone.TryAngle.common.code.BaseErrorCode;
import capstone.TryAngle.common.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum
ErrorStatus implements BaseErrorCode {


    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5000", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON4000", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON4001", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON4003", "금지된 요청입니다."),
    MISSING_REQUIRED_VALUE(HttpStatus.BAD_REQUEST, "COMMON4000", "입력되지 않은 필수값이 있습니다."),

    // 사용자 관련 응답
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4004", "사용자를 찾을 수 없습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH4001", "비밀번호가 올바르지 않습니다"),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "USER4009", "이미 사용중인 닉네임입니다."),
    NO_FOLLOW_INFO(HttpStatus.NOT_FOUND, "USER4008", "팔로우 정보가 존재하지 않습니다"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "USER4009", "이미 사용중인 이메일입니다."),
    CANNOT_FOLLOW_SELF(HttpStatus.CONFLICT, "USER4009", "내 닉네임은 팔로우할 수 없습니다."),
    ALREADY_FOLLOWING(HttpStatus.CONFLICT, "USER4009", "이미 팔로잉 중입니다."),
    NOT_FOLLOWING(HttpStatus.NOT_FOUND, "USER4004", "팔로잉 하고 있지 않습니다."),
    CANNOT_REPORT_SELF(HttpStatus.CONFLICT, "USER4009", "내 닉네임은 신고할 수 없습니다."),
    INVALID_WITHDRAWAL_MONEY(HttpStatus.BAD_REQUEST, "USER4000", "출금은 5000원 이상, 1000원 단위로 가능합니다."),

    // 챌린지 관련 응답
    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE404", "챌린지 정보가 존재하지 않습니다"),

    // 참여 관련 응답
    PARTICIPATION_NOT_FOUND(HttpStatus.NOT_FOUND, "PARTICIPATION404" ,"해당 챌린지에 해당 유저가 참여한 정보가 없습니다." );
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;


    }
}