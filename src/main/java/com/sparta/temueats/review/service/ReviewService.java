package com.sparta.temueats.review.service;

import com.sparta.temueats.review.dto.request.CreateReviewRequestDto;
import com.sparta.temueats.review.dto.response.CreateResponseDto;
import com.sparta.temueats.review.entity.P_review;
import com.sparta.temueats.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;


    public CreateResponseDto createReview(Long storeId, CreateReviewRequestDto createReviewRequestDto) {
        //가게 존재여부 확인
        //본인이 이용한 가게 확인


        P_review saveReview=reviewRepository.save(
                P_review.builder()
                        .useYn(true)
                        .score(createReviewRequestDto.getScore())
                        .content(createReviewRequestDto.getContent())
                        .reportYn(true)
                        .build()
        );

        CreateResponseDto createResponseDto= CreateResponseDto.builder()
                .code(1)
                .message("리뷰가 작성되었습니다.")
                .build();


        return createResponseDto;
    }
}
