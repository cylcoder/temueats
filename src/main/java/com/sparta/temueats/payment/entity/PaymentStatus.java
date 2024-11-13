package com.sparta.temueats.payment.entity;

public enum PaymentStatus {

    READY("결제 준비 완료"),
    PAID("결제 완료"),
    FAIL("결제 실패"),
    CANCELED("결제 취소");

    PaymentStatus(String status) {
    }
}
