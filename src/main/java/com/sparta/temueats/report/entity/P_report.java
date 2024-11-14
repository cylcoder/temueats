package com.sparta.temueats.report.entity;

import com.sparta.temueats.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_REPORT")
public class P_report extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID reportId;

    @Column(nullable = true)
    private String reportDetail;

    @Column(nullable = true)
    private boolean resolvedYn;

    @Column
    private LocalDateTime resolvedDate;

    @Builder
    public P_report(String reportDetail, boolean resolvedYn, LocalDateTime resolvedDate) {
        this.reportDetail = reportDetail;
        this.resolvedYn = resolvedYn;
        this.resolvedDate = resolvedDate;
    }
}
