package com.sparta.temueats.review.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CreateStoreReportReq {
    private UUID storeId;
    private String content;
}
