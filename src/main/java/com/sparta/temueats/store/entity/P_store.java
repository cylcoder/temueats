package com.sparta.temueats.store.entity;

import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.user.entity.P_user;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.geo.Point;

import java.util.UUID;

@Entity(name = "P_STORE")
@Builder
public class P_store extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private P_user user;

    @Column(length = 15, nullable = false)
    private String name;

    @Column(length = 500)
    private String image;

    @Column(length = 15, nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreState state;

    @Column(nullable = false)
    private Integer leastPrice;

    @Column(name = "del_price", nullable = false)
    private Integer deliveryPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private Point latLng;

    @Column(length = 50, nullable = false)
    private String address;

}
