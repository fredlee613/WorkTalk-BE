package com.golfzonTech4.worktalk.dto.pay;

import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class PayDto {

    private Long payId; // 결제 데이터 고유 키

    private Long reserveId; // 해당 결제가 이루어진 예약건

    private String impUid; // 아임포트 결제 거래 번호

    private String merchantUid; // 가맹점 고유 번호

    private String customerUid; // 개인 빌링키

    private PaymentStatus payStatus; // 결제 유형 (DEPOSIT, PREPAID, POSTPAID, REFUND)

    private int payAmount; // 결제 금액


    @QueryProjection
    public PayDto(Long payId, Long reserveId, String impUid, String merchantUid, String customerUid, PaymentStatus payStatus, int payAmount) {
        this.payId = payId;
        this.reserveId = reserveId;
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.customerUid = customerUid;
        this.payStatus = payStatus;
        this.payAmount = payAmount;
    }
}
