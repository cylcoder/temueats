package com.sparta.temueats.review.dto.response;

import com.sparta.temueats.review.entity.P_review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReviewResDto {

    private UUID reviewId;
    private String content;
    private int score;

    public ReviewResDto(P_review pReview) {
        this.reviewId = pReview.getReviewId();
        this.content = pReview.getContent();
        this.score = pReview.getScore();
    }

}
