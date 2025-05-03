package capstone.TryAngle.common;
import capstone.TryAngle.common.code.BaseErrorCode;
import capstone.TryAngle.common.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;
    public GeneralException(BaseErrorCode code) {
        super(code.getReason().getMessage());
        this.code = code;
    }

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}