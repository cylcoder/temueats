package com.sparta.temueats.review.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_REVIEWCOMMENT")
public class P_reviewComment {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID commentId;

    @Column(nullable = true)
    private String content;

    @Column(nullable = true)
    private boolean visibleYn;

}
