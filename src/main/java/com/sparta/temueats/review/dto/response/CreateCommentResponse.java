package com.sparta.temueats.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCommentResponse {
    private int code;
    private String message;

    @Builder
    public CreateCommentResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
