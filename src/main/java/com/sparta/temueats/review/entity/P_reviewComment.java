package com.sparta.temueats.review.entity;

import com.sparta.temueats.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_REVIEW_COMMENT")
public class P_reviewComment extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID commentId;

    @Column(nullable = true)
    private String content;

    @Column(nullable = true)
    private boolean visibleYn;

}
