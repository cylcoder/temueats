package com.sparta.temueats.review.controller;


import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.review.dto.request.CreateCommentRequest;
import com.sparta.temueats.review.dto.request.DeleteCommentResponse;
import com.sparta.temueats.review.dto.response.CreateCommentResponse;
import com.sparta.temueats.review.service.ReviewCommentService;
import com.sparta.temueats.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name="리뷰 댓글 생성/삭제")
@RestController
@RequestMapping("/api/reviews/comment")
@RequiredArgsConstructor
public class ReviewCommentController {
    private final ReviewCommentService reviewCommentService;

    @Operation(summary = "리뷰 댓글 작성")
    @PostMapping
    public ResponseDto<CreateCommentResponse> createComment(@RequestBody CreateCommentRequest createCommentRequest,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
            CreateCommentResponse createCommentResponse = reviewCommentService.createComment(createCommentRequest,
                    userDetails.getUser().getId());

            return new ResponseDto<>(createCommentResponse.getCode(), createCommentResponse.getMessage(), null);
    }

    @Operation(summary = "리뷰 댓글 삭제")
    @DeleteMapping("/{review_id}")
    public ResponseDto<DeleteCommentResponse> deleteComment(@PathVariable UUID review_id,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
            DeleteCommentResponse deleteCommentResponse=reviewCommentService.deleteComment(review_id, userDetails.getUser().getId());

            return new ResponseDto<>(deleteCommentResponse.getCode(), deleteCommentResponse.getMessage(), null);
    }
}
