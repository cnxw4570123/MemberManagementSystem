package com.sparta.mms.common.exception;

import static com.sparta.mms.common.exception.ErrorCode.*;

import com.sparta.mms.common.dto.response.Error;
import com.sparta.mms.common.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "예외 핸들러")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AbstractBusinessException.class})
    public ResponseEntity<Response> handleBusinessException(AbstractBusinessException e) {
        Error error = Error.of(e.getHttpStatus(), e.getCode(), e.getMessage());
        return error.toResponseEntity();
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Response> handleException(Exception e) {
        log.error("Internal Server Error: {}", e.getMessage());
        Error error = Error.of(
            TEMPORARY_SERVER_ISSUE.getHttpStatus(),
            TEMPORARY_SERVER_ISSUE.getCode(),
            TEMPORARY_SERVER_ISSUE.getMessage()
        );
        return error.toResponseEntity();
    }
}
