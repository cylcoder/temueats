package com.sparta.temueats.payment.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_PAYMENT")
public class P_payment {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID paymentID;

    @Column(nullable = true)
    private String state;

    @Column(nullable = true)
    private int price;


}
