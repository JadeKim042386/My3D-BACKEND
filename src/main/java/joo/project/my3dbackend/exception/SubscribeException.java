package joo.project.my3dbackend.exception;

import joo.project.my3dbackend.exception.constants.ErrorCode;
import lombok.Getter;

@Getter
public class SubscribeException extends CustomException {
    public SubscribeException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SubscribeException(ErrorCode errorCode, Exception causeException) {
        super(errorCode, causeException);
    }
}
