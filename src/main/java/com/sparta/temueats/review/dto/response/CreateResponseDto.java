package com.sparta.temueats.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@NoArgsConstructor
public class CreateResponseDto {
    private String message;
    private int code;

    @Builder
    public CreateResponseDto(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
