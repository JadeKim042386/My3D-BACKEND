package joo.project.my3dbackend.exception;

import joo.project.my3dbackend.exception.constants.ErrorCode;
import lombok.Getter;

@Getter
public class LikeException extends CustomException {
    public LikeException(ErrorCode errorCode) {
        super(errorCode);
    }

    public LikeException(ErrorCode errorCode, Exception causeException) {
        super(errorCode, causeException);
    }
}
