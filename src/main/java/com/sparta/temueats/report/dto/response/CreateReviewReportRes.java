package com.sparta.temueats.report.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateReviewReportRes {
    private ReviewInfoRes reviewInfoRes;
    private int code;
    private String message;

    @Builder
    public CreateReviewReportRes(ReviewInfoRes reviewInfoRes, int code, String message) {
        this.reviewInfoRes = reviewInfoRes;
        this.code = code;
        this.message = message;
    }
}
