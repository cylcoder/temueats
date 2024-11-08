package com.sparta.temueats.review.entity;

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
