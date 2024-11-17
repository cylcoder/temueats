package com.sparta.temueats.rating.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetRatingRes {
    private int code;
    private String message;
    private GetRating getRating;

    @Builder
    public GetRatingRes(int code,String message, GetRating getRating) {
        this.code = code;
        this.message = message;
        this.getRating = getRating;
    }
}
