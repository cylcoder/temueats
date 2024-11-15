package com.sparta.temueats.report.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ResolvedReportReq {
    private UUID reportId;
}
