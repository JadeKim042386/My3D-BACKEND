package joo.project.my3dbackend.exception;

import joo.project.my3dbackend.dto.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> resolveException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException is occurred.", e);
        String message = "Validation failed for argument in "
                + e.getParameter().getExecutable().getName();
        return ResponseEntity.status(BAD_REQUEST)
                .body(ExceptionResponse.fromBindingResult(message, e.getBindingResult()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> resolveException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException is occurred.", e);
        return ResponseEntity.status(BAD_REQUEST).body(ExceptionResponse.of(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> resolveException(Exception e) {
        log.error("Exception is occurred.", e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ExceptionResponse.of(e.getMessage()));
    }

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ExceptionResponse> resolveException(CustomException e) {
        log.error("CustomException is occurred.", e);
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(ExceptionResponse.of(e.getErrorCode().getMessage()));
    }
}
