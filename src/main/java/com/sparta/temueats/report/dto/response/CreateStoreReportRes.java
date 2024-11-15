package com.sparta.temueats.report.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateStoreReportRes {
    private StoreInfoRes storeInfoRes;
    private int code;
    private String message;

    @Builder
    public CreateStoreReportRes(StoreInfoRes storeInfoRes, int code, String message) {
        this.storeInfoRes = storeInfoRes;
        this.code = code;
        this.message = message;
    }
}
