package com.sparta.temueats.rating.entity;

import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.store.entity.P_store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity(name = "P_RATING")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class P_rating extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID ratingId;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private P_store store;

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    private boolean visibleYn;

}
