package joo.project.my3dbackend.exception;

import joo.project.my3dbackend.exception.constants.ErrorCode;
import lombok.Getter;

@Getter
public class SignUpException extends CustomException {
    public SignUpException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SignUpException(ErrorCode errorCode, Exception causeException) {
        super(errorCode, causeException);
    }
}
