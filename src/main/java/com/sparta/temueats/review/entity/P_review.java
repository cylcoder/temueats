package com.sparta.temueats.review.entity;

import com.sparta.temueats.store.entity.P_store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_REVIEW")
public class P_review {

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

    @Column(nullable = true)
    private float storeScore;


}
