package com.sparta.temueats.report.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.report.dto.request.CreateReviewReportReq;
import com.sparta.temueats.report.dto.request.ReportStoreInfoReq;
import com.sparta.temueats.report.dto.request.ResolvedReportReq;
import com.sparta.temueats.report.dto.response.*;
import com.sparta.temueats.report.service.ReportService;
import com.sparta.temueats.review.dto.request.CreateStoreReportReq;
import com.sparta.temueats.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="신고 생성/조회/처리")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @Operation(summary = "리뷰 신고")
    @PostMapping("/reviews")
    public ResponseDto<ReviewInfoRes> createReviewReport(
            @RequestBody CreateReviewReportReq createReviewReportReq ,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        CreateReviewReportRes createReviewReportRes=reportService.createReviewReport(createReviewReportReq,userDetails.getUser().getId());

        return new ResponseDto<>(createReviewReportRes.getCode(),
                createReviewReportRes.getMessage(),
                createReviewReportRes.getReviewInfoRes());
    }

    @Operation(summary = "가게 신고")
    @PostMapping("/stores")
    public ResponseDto<StoreInfoRes> createStoreReport(
            @RequestBody CreateStoreReportReq createStoreReportReq,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        CreateStoreReportRes createStoreReportRes=reportService.createStoreReport(createStoreReportReq,userDetails.getUser().getId());

        return new ResponseDto<>(createStoreReportRes.getCode(),
                createStoreReportRes.getMessage(),
                createStoreReportRes.getStoreInfoRes());
    }

    @Operation(summary = "신고 목록 조회")
    @GetMapping()
    public ResponseDto<List<ReportStoreInfoRes>> getReport(
            @RequestParam(name="page",defaultValue = "1") String page,
            @RequestParam(name="size",defaultValue = "10") String size,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        ReportStoreInfoReq reportStoreInfoReq= ReportStoreInfoReq.builder()
                .userId(userDetails.getUser().getId())
                .pageRequest(PageRequest.of(Integer.parseInt(page)-1,Integer.parseInt(size)))
                .build();
        ReportStoreInfoResList reportStoreInfoResList =reportService.getReport(reportStoreInfoReq);

        return new ResponseDto<>(reportStoreInfoResList.getCode(),
                reportStoreInfoResList.getMessage(),
                reportStoreInfoResList.getReportStoreInfoResList());
    }

    @Operation(summary = "신고 처리")
    @PutMapping()
    public ResponseDto<ResolvedReportRes> resolvedReport(
            @RequestBody ResolvedReportReq resolvedReportReq,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        ResolvedReportRes resolvedReportRes=reportService.resolvedReport(resolvedReportReq,userDetails.getUser().getId());
        return new ResponseDto<>(resolvedReportRes.getCode(), resolvedReportRes.getMessage(),null);
    }
}
