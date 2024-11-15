package com.sparta.temueats.report.service;

import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.report.dto.request.CreateReviewReportReq;
import com.sparta.temueats.report.dto.request.ReportStoreInfoReq;
import com.sparta.temueats.report.dto.response.*;
import com.sparta.temueats.report.entity.P_report;
import com.sparta.temueats.report.repository.ReportRepository;
import com.sparta.temueats.review.dto.request.CreateStoreReportReq;
import com.sparta.temueats.review.entity.P_review;
import com.sparta.temueats.review.repository.ReviewRepository;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.P_storeReq;
import com.sparta.temueats.store.repository.StoreRepository;
import com.sparta.temueats.store.repository.StoreReqRepository;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final UserService userService;
    private final StoreReqRepository storeReqRepository;

    public CreateReviewReportRes createReviewReport(CreateReviewReportReq createReviewReportReq, P_user user) {
        P_review review = reviewRepository.findById(createReviewReportReq.getReviewId()).orElseThrow(()->
                new CustomApiException("리뷰가 존재하지 않습니다."));
        if(Objects.equals(review.getUser().getId(), user.getId())){
            throw new CustomApiException("본인 리뷰는 신고할 수 없습니다.");
        }
        P_report newReport=reportRepository.save(
                P_report.builder()
                        .reportDetail(createReviewReportReq.getContent())
                        .storeName(createReviewReportReq.getStoreName())
                        .resolvedYn(false)
                        .resolvedDate(null)
                        .build()
        );
        ReviewInfoRes reviewInfoRes = ReviewInfoRes.builder()
                .createdAt(LocalDateTime.now())
                .nickname(user.getNickname())
                .build();
        return CreateReviewReportRes.builder()
                .reviewInfoRes(reviewInfoRes)
                .code(1)
                .message("리뷰가 신고되었습니다.")
                .build();
    }

    public CreateStoreReportRes createStoreReport(CreateStoreReportReq createStoreReportReq, P_user user) {
        P_store store=storeRepository.findById(createStoreReportReq.getStoreId()).orElseThrow(()->
                new CustomApiException("해당 가게는 존재하지 않습니다.")
        );
        P_report newReport=reportRepository.save(
                P_report.builder()
                        .reportDetail(createStoreReportReq.getContent())
                        .storeName(store.getName())
                        .resolvedYn(false)
                        .resolvedDate(null)
                        .build()
        );
        StoreInfoRes storeInfoRes= StoreInfoRes.builder()
                .storeName(store.getName())
                .content(createStoreReportReq.getContent())
                .build();
        return CreateStoreReportRes.builder()
                .storeInfoRes(storeInfoRes)
                .code(1)
                .message("가게가 신고되었습니다.")
                .build();
    }

    public ReportStoreInfoResList getStoreReport(ReportStoreInfoReq reportStoreInfoReq) {
        P_user user=userService.findUserById(reportStoreInfoReq.getUserId());
        Pageable pageable = PageRequest.of(reportStoreInfoReq.getPageRequest().getPageNumber(),
                reportStoreInfoReq.getPageRequest().getPageSize());
        // 레포지토리에서 페이징된 결과 조회
        switch (user.getRole()){
            case MASTER:{
                //모든가게 신고목록 조회
                Page<P_report> reportPage = reportRepository.findAll(pageable);
                reportStore(reportPage);
            }
            case MANAGER:{
                //모든가게 신고목록 조회
                Page<P_report> reportPage = reportRepository.findAll(pageable);
                reportStore(reportPage);
            }
            case OWNER:{
                //내 가게 신고목록 조회
                P_storeReq store =storeReqRepository.findByRequestedBy_Id(user.getId());
                Page<P_report> reportPage = reportRepository.findByStoreName(store.getName(),pageable);
                reportStore(reportPage);
            }
            case CUSTOMER:{
                //유저가 신고한 목록만 조회
                Page<P_report> reportPage = reportRepository.findByUserId(user.getId(), pageable);
                reportStore(reportPage);
            }
            default:
              throw new CustomApiException("권한이 없습니다.");
        }
    }

    // 신고목록 조회
    public ReportStoreInfoResList reportStore(Page<P_report> reportPage) {
        List<ReportStoreInfoRes> reportStoreInfoResList=new ArrayList<>();
        for(P_report report : reportPage.getContent()){
            ReportStoreInfoRes reportStoreInfoRes= ReportStoreInfoRes.builder()
                    .reportId(report.getReportId())
                    .content(report.getReportDetail())
                    .storeName(report.getStoreName())
                    .createdBy(report.getCreatedBy())
                    .createdAt(report.getCreatedAt())
                    .updatedAt(report.getUpdatedAt())
                    .resolvedYn(report.isResolvedYn())
                    .resolvedAt(report.getResolvedDate())
                    .build();
            reportStoreInfoResList.add(reportStoreInfoRes);
        }
        return ReportStoreInfoResList.builder()
                .reportStoreInfoResList(reportStoreInfoResList)
                .code(1)
                .message("가게 신고목록 조회")
                .build();
    }

}
