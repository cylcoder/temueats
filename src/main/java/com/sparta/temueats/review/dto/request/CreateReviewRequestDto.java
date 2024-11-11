package com.sparta.temueats.review.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateReviewRequestDto {

    private String nickname;
    private String content;
    private int score;

    @Builder
    public CreateReviewRequestDto(String nickname, String content, int score) {
        this.nickname = nickname;
        this.content = content;
        this.score = score;
    }

}
