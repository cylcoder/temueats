package com.sparta.temueats.store.entity;

import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.user.entity.P_user;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.locationtech.jts.geom.Point;

import java.util.UUID;

@Entity(name = "P_STORE_REQ")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class P_storeReq extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID storeReqId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private P_user requestedBy;

    @Column(length = 15, nullable = false)
    private String name;

    @Column(length = 500)
    private String image;

    @Column(length = 15, nullable = false)
    private String number;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreReqState state;

    public void update(StoreReqState storeReqState) {
        state = storeReqState;
    }

}
