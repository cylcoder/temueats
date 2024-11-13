//
//package com.sparta.temueats.review.service;
//
//import com.sparta.temueats.review.dto.request.CreateReviewRequestDto;
//import com.sparta.temueats.review.dto.response.CreateResponseDto;
//import com.sparta.temueats.review.dto.response.MyReviewReadResponseList;
//import com.sparta.temueats.review.dto.response.MyReviewResponse;
//import com.sparta.temueats.review.entity.P_review;
//import com.sparta.temueats.review.repository.ReviewRepository;
//import com.sparta.temueats.user.entity.P_user;
//import com.sparta.temueats.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ReviewService {
//    private final ReviewRepository reviewRepository;
//    private final UserRepository userRepository;
//
//
//    public CreateResponseDto createReview(Long storeId, CreateReviewRequestDto createReviewRequestDto) {
//        //가게 존재여부 확인
//        //본인이 이용한 가게 확인
//
//
//        P_review saveReview=reviewRepository.save(
//                P_review.builder()
//                        .useYn(true)
//                        .score(createReviewRequestDto.getScore())
//                        .content(createReviewRequestDto.getContent())
//                        .reportYn(true)
//                        .build()
//        );
//
//        CreateResponseDto createResponseDto= CreateResponseDto.builder()
//                .code(1)
//                .message("리뷰가 작성되었습니다.")
//                .build();
//
//
//        return createResponseDto;
//    }
//
//    public MyReviewReadResponseList myReviewRead(Long userId) {
//        P_user user=userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("해당 아이디는 존재하지 않습니다."));
//        String nickname=user.getNickname();
//        //userReview목록조회
//        List<P_review> myReviewList =reviewRepository.findByUserId(userId);
//        List<MyReviewResponse> myReviewResponseList = new ArrayList<>();
//
//        for(P_review review : myReviewList){
//            MyReviewResponse myReviewResponse= MyReviewResponse.builder()
//                    .reviewId(review.getReviewId())
//                    .content(review.getContent())
//                    .score(review.getScore())
//                    .storeName(review.getStore().getName())
//                    .createdAt(review.getCreatedAt())
//                    .updatedAt(review.getUpdatedAt())
//                    .build();
//
//            myReviewResponseList.add(myReviewResponse);
//        }
//
//        return MyReviewReadResponseList.builder()
//                .myReviewResponseList(myReviewResponseList)
//                .nickname(nickname)
//                .code(1)
//                .message("리뷰목록 조회 성공")
//                .build();
//
//    }
//}
//
