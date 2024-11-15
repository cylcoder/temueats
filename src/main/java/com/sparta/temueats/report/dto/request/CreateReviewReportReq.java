package com.sparta.temueats.report.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CreateReviewReportReq {
    private UUID reviewId;
    private String storeName;
    private String content;

}
