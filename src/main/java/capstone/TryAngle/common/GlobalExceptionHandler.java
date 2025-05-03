package capstone.TryAngle.common;

import capstone.TryAngle.common.code.ErrorReasonDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorReasonDTO> handleGeneralException(GeneralException ex) {
        ErrorReasonDTO errorResponse = ex.getErrorReasonHttpStatus();
        return ResponseEntity
                .status(errorResponse.getHttpStatus())
                .body(errorResponse);
    }
}
