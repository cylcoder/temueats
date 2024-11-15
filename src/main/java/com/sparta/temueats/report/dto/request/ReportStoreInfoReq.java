package com.sparta.temueats.report.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Getter
@NoArgsConstructor
public class ReportStoreInfoReq {
    private Long userId;
    private PageRequest pageRequest;

    @Builder
    public ReportStoreInfoReq(Long userId, PageRequest pageRequest) {
        this.userId = userId;
        this.pageRequest = pageRequest;
    }
}
