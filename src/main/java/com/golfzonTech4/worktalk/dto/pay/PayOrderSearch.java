package com.golfzonTech4.worktalk.dto.pay;

import com.golfzonTech4.worktalk.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderSearch {

    private PaymentStatus payStatus; // 결제 유형
    private LocalDateTime reserveDate; // 예약 날짜

}
