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
    INVALID_FIlE(HttpStatus.BAD_REQUEST, "파일이 비어있거나 지원되지 않는 확장자입니다. 지원되는 확장자는 *.stp, *.stl입니다."),
    INVALID_FIlE_NAME(HttpStatus.BAD_REQUEST, "파일 형식이 잘못되었습니다."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "articleCateogy가 null이거나 값이 잘못되었습니다."),
    INVALID_DIMENSION(HttpStatus.BAD_REQUEST, "dimensionOption이 null이거나 값이 잘못되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "게시글을 볼 수 있는 권한이 없습니다."),
    // Auth
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "Token이 만료되었습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    // File
    FILE_CANT_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다."),
    FILE_CANT_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 저장 할 수 없습니다. 파일 경로를 다시 확인해주세요."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    FAILED_DOWNLOAD_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 다운로드에 실패했습니다."),
    // Mail
    MAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송 실패"),
    // Sign Up
    INVALID_COMPANY_NAME(HttpStatus.BAD_REQUEST, "기업명이 입력되지않았습니다."),
    INVALID_CODE(HttpStatus.BAD_REQUEST, "코드를 입력해주세요."),
    EXPIRED_CODE(HttpStatus.BAD_REQUEST, "코드가 만료되었습니다. 코드 전송을 재요청해주세요."),
    // Alarm
    ALARM_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알람을 위한 연결 시도 실패"),
    NOT_FOUND_ALARM(HttpStatus.NOT_FOUND, "존재하지 않는 알람입니다."),
    CANT_GET_FOLDER(HttpStatus.INTERNAL_SERVER_ERROR, "메일 폴더를 가져올 수 없습니다."),
    CANT_GET_MAIL(HttpStatus.INTERNAL_SERVER_ERROR, "폴더에서 메일을 가져올 수 없습니다."),
    // Subscribe
    NOT_FOUND_SUBSCRIBE(HttpStatus.NOT_FOUND, "구독 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
