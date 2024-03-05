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
    NOT_FOUND_ARTICLE(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
    // Auth
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "Token이 만료되었습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
