package com.sparta.temueats.menu.entity;

import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.user.entity.P_user;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity(name = "P_AI_LOG")
public class P_aiLog extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private P_user requestedBy;

    @Column(nullable = false)
    private String request;

    @Column(nullable = false)
    private String response;

}
