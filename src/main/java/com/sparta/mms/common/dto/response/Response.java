package com.sparta.mms.common.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface Response {

    HttpStatus getStatus();

    default ResponseEntity<Response> toResponseEntity() {
        return ResponseEntity.status(getStatus()).body(this);
    }
}
