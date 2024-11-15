package com.sparta.temueats.report.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ReportStoreInfoRes {
    private UUID reportId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private boolean resolvedYn;
    private LocalDateTime resolvedAt;

    @Builder
    public ReportStoreInfoRes(UUID reportId, String content, LocalDateTime createdAt,
                              LocalDateTime updatedAt,String createdBy,
                              boolean resolvedYn, LocalDateTime resolvedAt) {
        this.reportId = reportId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.resolvedYn = resolvedYn;
        this.resolvedAt = resolvedAt;
    }
}


