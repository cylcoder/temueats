
package com.sparta.temueats.review.service;

import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.review.dto.request.CreateReviewRequestDto;
import com.sparta.temueats.review.dto.response.*;
import com.sparta.temueats.review.entity.P_review;
import com.sparta.temueats.review.repository.ReviewRepository;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.repository.StoreRepository;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.repository.UserRepository;
import com.sparta.temueats.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final UserService userService;

    public CreateResponseDto createReview(UUID storeId, Long userId ,CreateReviewRequestDto createReviewRequestDto) {
        //가게 존재여부 확인
        P_store store= storeRepository.findById(storeId).orElseThrow(()->
                new CustomApiException("해당 가게가 존재하지 않습니다."));
        //본인이 이용한 가게 확인..어떻게..?
        P_user user=userService.findUserById(userId);


        P_review saveReview=reviewRepository.save(
                P_review.builder()
                        .useYn(true)
                        .score(createReviewRequestDto.getScore())
                        .content(createReviewRequestDto.getContent())
                        .reportYn(true)
                        .user(user)
                        .store(store)
                        .build()
        );

        CreateResponseDto createResponseDto= CreateResponseDto.builder()
                .code(1)
                .message("리뷰가 작성되었습니다.")
                .build();


        return createResponseDto;
    }

    public MyReviewReadResponseList getMyReviews(Long userId) {
        //userService로 유저정보 가져오기
        P_user user = userService.getUser();
        String nickname=user.getNickname();

        //userReview목록조회
        List<P_review> myReviewList =reviewRepository.findByUserId(userId);
        List<MyReviewResponse> myReviewResponseList = new ArrayList<>();

        for(P_review review : myReviewList){
            MyReviewResponse myReviewResponse= MyReviewResponse.builder()
                    .reviewId(review.getReviewId())
                    .content(review.getContent())
                    .score(review.getScore())
                    .storeName(review.getStore().getName())
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .build();

            myReviewResponseList.add(myReviewResponse);
        }

        return MyReviewReadResponseList.builder()
                .myReviewResponseList(myReviewResponseList)
                .nickname(nickname)
                .code(1)
                .message("나의 리뷰목록 조회 성공")
                .build();

    }

    public StoreReviewResponseList getStoreReviews(UUID storeId) {
        P_store store=reviewRepository.findStoreByStoreId(storeId).
                orElseThrow(()->new IllegalArgumentException("가게가 존재하지 않습니다."));

        List<P_review> reviewList=reviewRepository.findByStoreId(storeId);
        List<StoreReviewResponse> storeReviewResponseList = new ArrayList<>();
        for(P_review review : reviewList){
            StoreReviewResponse storeReviewResponse= StoreReviewResponse.builder()
                    .reviewId(review.getReviewId())
                    .content(review.getContent())
                    .nickname(review.getStore().getName())
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .build();

            storeReviewResponseList.add(storeReviewResponse);
        }

        return StoreReviewResponseList.builder()
                .storeReviewResponses(storeReviewResponseList)
                .code(1)
                .message("가게 리뷰목록 조회 성공")
                .build();
    }

    @Transactional
    public DeleteReviewResponse deleteReviews(UUID reviewId,Long userId) {
        //리뷰 불러오기
        P_review review=reviewRepository.findById(reviewId)
                .orElseThrow(()->new IllegalArgumentException("리뷰가 존재하지 않습니다."));

       //user일치 확인
        if(!review.getUser().getId().equals(userId)){
            return new DeleteReviewResponse(-1,"권한이 없습니다.");
        }

        review.changeUseYn();
        reviewRepository.save(review);

        return new DeleteReviewResponse(1,"리뷰 삭제가 완료 되었습니다.");

    }
}

