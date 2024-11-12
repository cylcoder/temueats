package com.sparta.temueats.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class MyReviewResponse {
    private UUID reviewId;
    private String storeName;
    private String content;
    private int score;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private MyReviewResponse(UUID reviewId, String storeName, String content, int score, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.reviewId = reviewId;
        this.storeName = storeName;
        this.content = content;
        this.score = score;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


}
