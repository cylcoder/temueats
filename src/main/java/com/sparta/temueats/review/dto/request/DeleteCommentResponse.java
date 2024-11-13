package com.sparta.temueats.review.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteCommentResponse {
    private int code;
    private String message;

    @Builder
    public DeleteCommentResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
