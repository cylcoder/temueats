package com.sparta.temueats.store.entity;


import com.sparta.temueats.user.entity.P_user;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;


@Entity(name="P_FAV_STORE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class P_favStore {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID favId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private P_user user;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private P_store store;

}
