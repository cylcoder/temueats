package com.sparta.temueats.report.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResolvedReportRes {
    private int code;
    private String message;

    @Builder
    public ResolvedReportRes(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
