package com.sparta.temueats.report.entity;

import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.user.entity.P_user;
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

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = true)
    private String reportDetail;

    @Column(nullable = true)
    private boolean resolvedYn;

    @Column
    private LocalDateTime resolvedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private P_user user;

    @Builder
    public P_report(String reportDetail,String storeName ,boolean resolvedYn, LocalDateTime resolvedDate) {
        this.reportDetail = reportDetail;
        this.storeName = storeName;
        this.resolvedYn = resolvedYn;
        this.resolvedDate = resolvedDate;
    }
}
