package joo.project.my3dbackend.exception;

import joo.project.my3dbackend.dto.response.ExceptionResponse;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import lombok.Getter;

@Getter
public class ValidatedException extends CustomException {
    private final ExceptionResponse exceptionResponse;

    public ValidatedException(ErrorCode errorCode, ExceptionResponse exceptionResponse) {
        super(errorCode);
        this.exceptionResponse = exceptionResponse;
    }

    public ValidatedException(ErrorCode errorCode, Throwable cause, ExceptionResponse exceptionResponse) {
        super(errorCode, cause);
        this.exceptionResponse = exceptionResponse;
    }
}
