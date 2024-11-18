package com.sparta.temueats.review.service;

import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.review.dto.request.CreateCommentRequest;
import com.sparta.temueats.review.dto.request.DeleteCommentResponse;
import com.sparta.temueats.review.dto.response.CreateCommentResponse;
import com.sparta.temueats.review.entity.P_review;
import com.sparta.temueats.review.entity.P_reviewComment;
import com.sparta.temueats.review.repository.ReviewCommentRepository;
import com.sparta.temueats.review.repository.ReviewRepository;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
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

    public CreateCommentResponse createComment(CreateCommentRequest createCommentRequest,Long userId) {
        P_review review =reviewRepository.findById(createCommentRequest.getReviewId()).
                orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));
        P_user user=userService.findUserById(userId);
        checkUser(user);
        if(!user.getRole().equals(UserRoleEnum.OWNER)){
            return new CreateCommentResponse(-1,"사장님만 작성할 수 있습니다.");
        }
        reviewCommentRepository.save(P_reviewComment.builder()
                .content(createCommentRequest.getContent())
                .visibleYn(true)
                .review(review)
                .build()
        );
        return new CreateCommentResponse(1,"댓글이 작성되었습니다.");
    }

    @Transactional
    public DeleteCommentResponse deleteComment(UUID reviewId, Long userId) {
        P_user user= userService.findUserById(userId);
        checkUser(user);
        if(user.getRole().equals(UserRoleEnum.OWNER)){
            return new DeleteCommentResponse(-1,"사장님만 삭제할 수 있습니다.");
        }

        P_reviewComment reviewComment=reviewCommentRepository.findByReview_ReviewId(reviewId).
                orElseThrow(()->new IllegalArgumentException("리뷰가 존재하지 않습니다."));
        reviewComment.changeVisibleYn();
        reviewCommentRepository.save(reviewComment);

        return new DeleteCommentResponse(1,"라뷰가 삭제되었습니다.");

    }

    public void checkUser(P_user user){
        if(user==null){
            throw new CustomApiException("해당 아이디가 없습니다.");
        }


    }
}
