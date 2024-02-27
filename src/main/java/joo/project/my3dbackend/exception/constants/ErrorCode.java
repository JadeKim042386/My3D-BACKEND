package joo.project.my3dbackend.exception.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common
    INTERNAL_SERVER_ERROR_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    // Article
    FAILED_WRITE_ARTICLE(HttpStatus.BAD_REQUEST, "게시글을 작성하지 못했습니다."),
    // Auth
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
