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

    @Column
    private int sum;

    @Column(nullable = false)
    private boolean visibleYn;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int count;

    @Builder
    public P_rating(P_store store, Double score, boolean visibleYn, int sum,int count) {
        this.store = store;
        this.score = score;
        this.sum =sum;
        this.count =count;
        this.visibleYn = visibleYn;
    }

    public void changeSum(int sum){
        this.sum = this.sum + sum;
        this.count = this.count + 1;
        this.score = (double) (this.sum/this.count);
    }

}
