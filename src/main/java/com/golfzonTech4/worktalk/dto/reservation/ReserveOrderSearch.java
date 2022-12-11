package com.golfzonTech4.worktalk.dto.reservation;

import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.domain.ReserveStatus;
import com.golfzonTech4.worktalk.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveOrderSearch {

    private int pageNum; // 페이지 번호
    private Integer paid; // 결제 여부
    private Integer spaceType; // 공간 타입
    private ReserveStatus reserveStatus; // 예약 상태
    private PaymentStatus paymentStatus; // 결제 종류
    private RoomType roomType; // 세부 공간 타입

}
