package com.sparta.temueats.review.service;

import com.sparta.temueats.review.dto.request.CreateCommentRequest;
import com.sparta.temueats.review.dto.request.DeleteCommentResponse;
import com.sparta.temueats.review.dto.request.DeleteCommnetRequest;
import com.sparta.temueats.review.dto.response.CreateCommentResponse;
import com.sparta.temueats.review.entity.P_review;
import com.sparta.temueats.review.entity.P_reviewComment;
import com.sparta.temueats.review.repository.ReviewCommentRepository;
import com.sparta.temueats.review.repository.ReviewRepository;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewRepository reviewRepository;
    private final UserService userService;

    public CreateCommentResponse createComment(CreateCommentRequest createCommentRequest) {
        P_review review =reviewRepository.findById(createCommentRequest.getReviewId()).
                orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));

        reviewCommentRepository.save(P_reviewComment.builder()
                .content(createCommentRequest.getContent())
                .visibleYn(true)
                .review(review)
                .build()
        );
        return new CreateCommentResponse(1,"댓글이 작성되었습니다.");
    }

    @Transactional
    public DeleteCommentResponse deleteComment(UUID reviewId, DeleteCommnetRequest deleteCommnetRequest) {
        P_user user= userService.getUser();
        if(user==null){
            return new DeleteCommentResponse(-1,"아이디가 존재하지 않습니다.");
        }
        P_reviewComment reviewComment=reviewCommentRepository.findByReview_ReviewId(reviewId).
                orElseThrow(()->new IllegalArgumentException("리뷰가 존재하지 않습니다."));
        reviewComment.changeVisibleYn();
        reviewCommentRepository.save(reviewComment);

        return new DeleteCommentResponse(1,"라뷰가 삭제되었습니다.");

    }
}
