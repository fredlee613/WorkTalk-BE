package com.golfzonTech4.worktalk.domain;

public enum ReserveStatus {
    BOOKED, // 예약 완료
    CANCELED_BY_USER, // 사용자 취소
    CANCELED_BY_HOST,  // 호스트 취소
    USED, // 사용완료
    NOSHOW // 노쇼
}
