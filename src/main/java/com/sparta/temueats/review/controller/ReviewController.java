
package com.sparta.temueats.review.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.review.dto.request.CreateReviewRequestDto;
import com.sparta.temueats.review.dto.request.DeleteReviewRequest;
import com.sparta.temueats.review.dto.request.MyReviewRequestDto;
import com.sparta.temueats.review.dto.request.StoreReviewRequest;
import com.sparta.temueats.review.dto.response.*;
import com.sparta.temueats.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{store_id}")
    public ResponseDto<CreateResponseDto> createReview(@PathVariable Long storeId,
                                                      @RequestBody CreateReviewRequestDto createReviewRequestDto){
        CreateResponseDto createResponseDto=reviewService.createReview(storeId,createReviewRequestDto);

        return new ResponseDto<>(createResponseDto.getCode(),createResponseDto.getMessage(),null);

    }

    @GetMapping
    public ResponseDto<List<MyReviewResponse>> getMyReviews(@RequestBody MyReviewRequestDto myReviewRequestDto){
        MyReviewReadResponseList myReviewReadResponseList= reviewService.getMyReviews(myReviewRequestDto.getUserId());

        return new ResponseDto<>(myReviewReadResponseList.getCode(),
                myReviewReadResponseList.getMessage(),
                myReviewReadResponseList.getMyReviewResponseList());

    }

    @GetMapping("/{store_id}")
    public ResponseDto<List<StoreReviewResponse>> getStoreReviews(@PathVariable UUID storeId){
        StoreReviewResponseList storeReviewResponseList=reviewService.getStoreReviews(storeId);

        return new ResponseDto<>(storeReviewResponseList.getCode(),
                storeReviewResponseList.getMessage(),
                storeReviewResponseList.getStoreReviewResponses());

    }

    @DeleteMapping("/{review_id}")
    public ResponseDto<DeleteReviewResponse> deleteReviews(@PathVariable UUID review_id,
            @RequestBody DeleteReviewRequest deleteReviewRequest){
        DeleteReviewResponse deleteReviewResponse=reviewService.deleteReviews(review_id,deleteReviewRequest.getUserId());

        return new ResponseDto<>(deleteReviewResponse.getCode(),
                deleteReviewResponse.getMessage(),
                null);

    }

}

