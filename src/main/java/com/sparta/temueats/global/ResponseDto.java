package com.sparta.temueats.global;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
public class ResponseDto<T> {
    private final Integer code; // 1 성공, -1 실패
    private final String msg;
    private final T data;

    public ResponseDto(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
