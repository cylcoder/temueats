package com.sparta.temueats.review.service;

import com.sparta.temueats.review.dto.request.CreateCommentRequest;
import com.sparta.temueats.review.dto.response.CreateCommentResponse;
import com.sparta.temueats.review.entity.P_review;
import com.sparta.temueats.review.entity.P_reviewComment;
import com.sparta.temueats.review.repository.ReviewCommentRepository;
import com.sparta.temueats.review.repository.ReviewRepository;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.sparta.temueats.dummy.DummyTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewCommentServiceTest {
    @InjectMocks
    private ReviewCommentService reviewCommentService;
    @Mock
    private ReviewCommentRepository reviewCommentRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserService userService;
    private P_store store;
    private P_user customer;
    private P_user owner;
    private P_review review;

    @BeforeEach
    void setUp() {
        customer = mockCustomerUserSetting();
        store = mockStoreSetting();
        review=mockReviewSetting();
        owner=mockOwnerUserSetting();
    }

    @Test
    @DisplayName("리뷰댓글생성")
    void CreateCommentSuccess() {
        when(reviewRepository.findById(review.getReviewId())).thenReturn(Optional.of(review));
        when(userService.findUserById(owner.getId())).thenReturn(owner);
        when(reviewCommentRepository.save(any(P_reviewComment.class))).thenReturn(newComment());
        CreateCommentRequest createCommentRequest= CreateCommentRequest.builder()
                .reviewId(review.getReviewId())
                .content("감사합니다.")
                .build();
        CreateCommentResponse createCommentResponse=reviewCommentService.createComment(createCommentRequest, owner.getId());

        assertEquals(1,createCommentResponse.getCode());
    }

    @Test
    @DisplayName("댓글 고객이 작성")
    void CreateCommentFail() {

    }

    private P_reviewComment newComment(){
        return P_reviewComment.builder()
                .content("감사합니다.")
                .visibleYn(true)
                .review(review)
                .build();
    }
}
