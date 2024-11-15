package com.sparta.temueats.report.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReportStoreInfoResList {
    private List<ReportStoreInfoRes> reportStoreInfoResList;
    private int code;
    private String message;

    @Builder
    public ReportStoreInfoResList(List<ReportStoreInfoRes> reportStoreInfoResList, int code, String message) {
        this.reportStoreInfoResList = reportStoreInfoResList;
        this.code = code;
        this.message = message;
    }
}
