package joo.project.my3dbackend.exception;

import joo.project.my3dbackend.exception.constants.ErrorCode;
import lombok.Getter;

@Getter
public class MailException extends CustomException {
    public MailException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MailException(ErrorCode errorCode, Exception causeException) {
        super(errorCode, causeException);
    }
}
