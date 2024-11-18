package com.sparta.temueats.rating.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.rating.dto.response.GetRating;
import com.sparta.temueats.rating.dto.response.GetRatingRes;
import com.sparta.temueats.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping("/{store_id}")
    public ResponseDto<GetRating> showRating(@PathVariable UUID store_id) {
        GetRatingRes getRatingRes = ratingService.getRating(store_id);
        return new ResponseDto<>(getRatingRes.getCode(),getRatingRes.getMessage(),getRatingRes.getGetRating());
    }

}
