package com.sparta.temueats.review.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.review.dto.request.CreateReviewRequestDto;
import com.sparta.temueats.review.dto.response.CreateResponseDto;
import com.sparta.temueats.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{store_id}")
    public ResponseDto<CreateResponseDto> createReview(@PathVariable Long store_id,
                                                      @RequestBody CreateReviewRequestDto createReviewRequestDto){
        CreateResponseDto createResponseDto=reviewService.createReview(store_id,createReviewRequestDto);

        return new ResponseDto<>(createResponseDto.getCode(),createResponseDto.getMessage(),null);

    }
}
