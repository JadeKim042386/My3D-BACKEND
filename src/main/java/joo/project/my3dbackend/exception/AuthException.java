package joo.project.my3dbackend.exception;

import joo.project.my3dbackend.exception.constants.ErrorCode;
import lombok.Getter;

@Getter
public class AuthException extends CustomException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(ErrorCode errorCode, Exception causeException) {
        super(errorCode, causeException);
    }
}
