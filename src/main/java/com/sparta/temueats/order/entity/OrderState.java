package com.sparta.temueats.order.entity;

public enum OrderState {

    STANDBY("결제 대기중"),
    PROGRESS("조리 & 배달중"),
    SUCCESS("고객 전달 완료"),
    FAIL("주문 실패");

    private final String status;
    OrderState(String status) {
        this.status = status;
    }
    public String getStatus() {return status;}
}
