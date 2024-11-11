package com.sparta.temueats.menu.entity;

import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.user.entity.P_user;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity(name = "P_AI_LOG")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class P_aiLog extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID aiLogId;

    @Column(nullable = false)
    private String request;

    @Column(nullable = false)
    private String response;

}
