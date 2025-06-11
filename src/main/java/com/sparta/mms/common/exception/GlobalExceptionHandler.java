package com.sparta.mms.common.exception;

import com.sparta.mms.common.dto.response.Error;
import com.sparta.mms.common.dto.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AbstractBusinessException.class})
    public ResponseEntity<Response> handleBusinessException(AbstractBusinessException e) {
        return ResponseEntity.status(e.getHttpStatus())
            .body(Error.of(e.getHttpStatus(), e.getCode(), e.getMessage()));
    }
}
