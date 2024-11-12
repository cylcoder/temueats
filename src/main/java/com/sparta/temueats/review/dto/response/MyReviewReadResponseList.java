package com.sparta.temueats.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MyReviewReadResponseList {

    private List<MyReviewResponse> myReviewResponseList;
    private String nickname;
    private String message;
    private int code;

    @Builder
    private MyReviewReadResponseList(List<MyReviewResponse> myReviewResponseList, String nickname, String message, int code) {
        this.myReviewResponseList = myReviewResponseList;
        this.nickname = nickname;
        this.message = message;
        this.code = code;
    }
}
