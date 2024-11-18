package com.sparta.temueats.review.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CreateCommentRequest {
    private UUID reviewId;
    private String content;

    @Builder
    public CreateCommentRequest(UUID reviewId, String content) {
        this.reviewId = reviewId;
        this.content = content;
    }
}
