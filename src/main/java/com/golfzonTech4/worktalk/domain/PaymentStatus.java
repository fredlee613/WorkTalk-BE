package com.golfzonTech4.worktalk.domain;

public enum PaymentStatus {
    DEPOSIT, // 보증금 결제
    PREPAID, // 선결제(완납)
    POSTPAID, // 후결제(결제 예약 안됨)
    POSTPAID_BOOKED, // 후결제(결제예정)
    POSTPAID_DONE, // 후결제(완납)
    REFUND // 환불
}
