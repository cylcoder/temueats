package com.sparta.temueats.review.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyReviewRequestDto {
    private Long userId;

    @Builder
    public MyReviewRequestDto(Long userId) {
        this.userId = userId;
    }
}
