package com.sparta.temueats.global.ex.handler;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.global.ex.entity.P_error;
import com.sparta.temueats.global.ex.repository.ErrorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static com.sparta.temueats.global.ResponseDto.FAILURE;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorRepository errorRepository;

    public static final String msg = "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.";

    @ExceptionHandler(Exception.class)
    public ResponseDto<String> handleAllExceptions(Exception e) {
        log.error("exception: ", e);
        save(e);
        return new ResponseDto<>(FAILURE, msg);
    }

    public void save(Exception e) {
        P_error error = P_error.builder()
                .exceptionType(e.getClass().getName())
                .message(e.getMessage())
                .stackTrace(getStackTraceAsString(e))
                .timestamp(LocalDateTime.now())
                .build();

        errorRepository.save(error);
    }

    private String getStackTraceAsString(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }

}
