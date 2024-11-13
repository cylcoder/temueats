package com.sparta.temueats.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StoreReviewResponseList {
    private List<StoreReviewResponse> storeReviewResponses;
    private int code;
    private String message;

    @Builder
    public StoreReviewResponseList(List<StoreReviewResponse> storeReviewResponses, int code, String message) {
        this.storeReviewResponses = storeReviewResponses;
        this.code = code;
        this.message = message;
    }

}
