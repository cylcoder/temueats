package com.sparta.temueats.report.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.report.dto.request.CreateReviewReportReq;
import com.sparta.temueats.report.dto.response.CreateReviewReportRes;
import com.sparta.temueats.report.dto.response.ReviewInfoRes;
import com.sparta.temueats.report.service.ReportService;
import com.sparta.temueats.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
