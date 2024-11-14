package com.sparta.temueats.report.service;

import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.report.dto.request.CreateReviewReportReq;
import com.sparta.temueats.report.dto.response.CreateReviewReportRes;
import com.sparta.temueats.report.dto.response.CreateStoreReportRes;
import com.sparta.temueats.report.dto.response.ReviewInfoRes;
import com.sparta.temueats.report.dto.response.StoreInfoRes;
import com.sparta.temueats.report.entity.P_report;
import com.sparta.temueats.report.repository.ReportRepository;
import com.sparta.temueats.review.dto.request.CreateStoreReportReq;
import com.sparta.temueats.review.entity.P_review;
import com.sparta.temueats.review.repository.ReviewRepository;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.repository.StoreRepository;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Store;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    public CreateReviewReportRes createReviewReport(CreateReviewReportReq createReviewReportReq, P_user user) {
        P_review review = reviewRepository.findById(createReviewReportReq.getReviewId()).orElseThrow(()->
                new CustomApiException("리뷰가 존재하지 않습니다."));
        if(Objects.equals(review.getUser().getId(), user.getId())){
            throw new CustomApiException("본인 리뷰는 신고할 수 없습니다.");
        }
        P_report newReport=reportRepository.save(
                P_report.builder()
                        .reportDetail(createReviewReportReq.getContent())
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
}
