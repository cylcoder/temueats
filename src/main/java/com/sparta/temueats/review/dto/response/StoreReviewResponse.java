package com.sparta.temueats.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class StoreReviewResponse {
    private UUID reviewId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public StoreReviewResponse(UUID reviewId, String nickname, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.reviewId = reviewId;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
