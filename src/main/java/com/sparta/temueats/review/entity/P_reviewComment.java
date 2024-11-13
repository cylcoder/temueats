package com.sparta.temueats.review.entity;

import com.sparta.temueats.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_REVIEWCOMMENT")
public class P_reviewComment extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID commentId;

    @Column(nullable = true)
    private String content;

    @Column(nullable = true)
    private boolean visibleYn;

    @OneToOne
    @JoinColumn(name = "review_id", nullable = false)  // review_id는 외래키
    private P_review review;

    @Builder
    public P_reviewComment(String content, boolean visibleYn, P_review review) {
        this.content = content;
        this.visibleYn = visibleYn;
        this.review = review;
    }

    public void changeVisibleYn() {
        this.visibleYn = false;
    }

}
