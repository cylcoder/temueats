package com.sparta.temueats.review.service;

import com.sparta.temueats.review.dto.request.CreateReviewRequestDto;
import com.sparta.temueats.review.dto.response.CreateResponseDto;
import com.sparta.temueats.review.entity.P_review;
import com.sparta.temueats.review.repository.ReviewRepository;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.repository.StoreRepository;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.sparta.temueats.dummy.DummyTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private UserService userService;
    private P_store store;
    private P_user user;
    @BeforeEach
    void setUp() {
       user = mockCustomerUserSetting();
       store = mockStoreSetting();
    }
    @Test
    @DisplayName("리뷰 생성")
    void CreateReview() {
        when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
        when(userService.findUserById(user.getId())).thenReturn(user);
        when(reviewRepository.save(any(P_review.class))).thenReturn(newReview());
        CreateReviewRequestDto createReviewRequestDto= CreateReviewRequestDto.builder()
                .nickname("유저")
                .content("너무맛있어요")
                .score(5)
                .build();
        CreateResponseDto createResponseDto= reviewService.createReview(store.getStoreId(), user.getId(),createReviewRequestDto);

        assertEquals(1,createResponseDto.getCode());
        assertEquals("리뷰가 작성되었습니다.",createResponseDto.getMessage());

    }


    private P_review newReview(){
        return P_review.builder()
                .useYn(true)
                .score(5)
                .content("너무맛있어요")
                .reportYn(true)
                .user(user)
                .store(store)
                .build();
    }
}
