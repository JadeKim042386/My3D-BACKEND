package joo.project.my3dbackend.exception;

import joo.project.my3dbackend.exception.constants.ErrorCode;
import lombok.Getter;

@Getter
public class AlarmException extends CustomException {
    public AlarmException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AlarmException(ErrorCode errorCode, Exception causeException) {
        super(errorCode, causeException);
    }
}
