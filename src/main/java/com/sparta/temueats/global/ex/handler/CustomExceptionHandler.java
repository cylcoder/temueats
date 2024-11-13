package com.sparta.temueats.global.ex.handler;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.global.ex.CustomApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e) {
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> AuthorizationDeniedException() {
        return new ResponseEntity<>(new ResponseDto<>(-1, "사용할 수 있는 권한이 없습니다.", null), HttpStatus.BAD_REQUEST);
    }
}