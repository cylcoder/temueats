package com.sparta.temueats.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteReviewResponse {
    private int code;
    private String message;

    @Builder
    public DeleteReviewResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
