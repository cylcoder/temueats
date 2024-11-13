package com.sparta.temueats.review.controller;


import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.review.dto.request.CreateCommentRequest;
import com.sparta.temueats.review.dto.response.CreateCommentResponse;
import com.sparta.temueats.review.service.ReviewCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews/comment")
@RequiredArgsConstructor
public class ReviewCommentController {
    private final ReviewCommentService reviewCommentService;

    @PostMapping
    public ResponseDto<CreateCommentResponse> createComment(@RequestBody CreateCommentRequest createCommentRequest){
            CreateCommentResponse createCommentResponse = reviewCommentService.createComment(createCommentRequest);

            return new ResponseDto<>(createCommentResponse.getCode(), createCommentResponse.getMessage(), null);
    }
}
