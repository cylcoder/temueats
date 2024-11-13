package com.sparta.temueats.review.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CreateCommentRequest {
    private UUID reviewId;
    private String content;
}
