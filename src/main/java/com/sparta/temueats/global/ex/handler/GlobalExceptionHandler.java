package com.sparta.temueats.global.ex.handler;

import com.sparta.temueats.global.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.sparta.temueats.global.ResponseDto.FAILURE;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String msg = "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.";

    @ExceptionHandler(Exception.class)
    public ResponseDto<String> handleAllExceptions(Exception ex) {
        log.error("exception: ", ex);
        return new ResponseDto<>(FAILURE, msg);
    }

}
