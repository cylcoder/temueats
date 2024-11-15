package com.sparta.temueats.report.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.report.dto.request.CreateReviewReportReq;
import com.sparta.temueats.report.dto.request.ReportStoreInfoReq;
import com.sparta.temueats.report.dto.response.*;
import com.sparta.temueats.report.service.ReportService;
import com.sparta.temueats.review.dto.request.CreateStoreReportReq;
import com.sparta.temueats.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repots")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/reviews")
    public ResponseDto<ReviewInfoRes> createReviewReport(
            @RequestBody CreateReviewReportReq createReviewReportReq ,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        CreateReviewReportRes createReviewReportRes=reportService.createReviewReport(createReviewReportReq,userDetails.getUser());

        return new ResponseDto<>(createReviewReportRes.getCode(),
                createReviewReportRes.getMessage(),
                createReviewReportRes.getReviewInfoRes());
    }

    @PostMapping("/stores")
    public ResponseDto<StoreInfoRes> createStoreReport(
            @RequestBody CreateStoreReportReq createStoreReportReq,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        CreateStoreReportRes createStoreReportRes=reportService.createStoreReport(createStoreReportReq,userDetails.getUser());

        return new ResponseDto<>(createStoreReportRes.getCode(),
                createStoreReportRes.getMessage(),
                createStoreReportRes.getStoreInfoRes());
    }

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
}
