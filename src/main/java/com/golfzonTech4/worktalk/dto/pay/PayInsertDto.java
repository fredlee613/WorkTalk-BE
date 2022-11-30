package com.golfzonTech4.worktalk.dto.pay;

import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.domain.Reservation;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.*;

@Data
@NoArgsConstructor
@ToString
public class PayInsertDto {

    private Long payId; // 결제 데이터 고유 키
    private Long reserveId; // 해당 결제가 이루어진 예약건
    private String customer_uid; // 고객 고유 빌링키
    private String imp_uid; // 아임포트 결제 거래 번호
    private String merchant_uid; // 가맹점 고유 번호
    private PaymentStatus payStatus; // 결제 유형 (DEPOSIT, PREPAID, POSTPAID, REFUND)
    private int payAmount; // 결제 금액

    @QueryProjection
    public PayInsertDto(Long payId, Long reserveId, String customer_uid, String imp_uid, String merchant_uid, PaymentStatus payStatus, int payAmount) {
        this.payId = payId;
        this.reserveId = reserveId;
        this.customer_uid = customer_uid;
        this.imp_uid = imp_uid;
        this.merchant_uid = merchant_uid;
        this.payStatus = payStatus;
        this.payAmount = payAmount;
    }
}
