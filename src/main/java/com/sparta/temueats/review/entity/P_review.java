package com.sparta.temueats.review.entity;

import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.user.entity.P_user;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_REVIEW")
public class P_review extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID reviewId;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private P_store store;

    @Column(nullable = true)
    private String content;

    @Column(nullable = true)
    private int score;

    @Column(nullable = true)
    private boolean useYn;

    @Column(nullable = true)
    private boolean reportYn;


    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private P_user user;


    @Builder
    public P_review(String content, int score, boolean useYn, boolean reportYn) {
        this.content = content;
        this.score = score;
        this.useYn = useYn;
        this.reportYn = reportYn;
    }
}
