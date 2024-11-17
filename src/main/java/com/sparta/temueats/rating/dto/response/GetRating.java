package com.sparta.temueats.rating.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetRating {
    private double rating;

    @Builder
    public GetRating(double rating) {
        this.rating = rating;
    }
}
