package com.golfzonTech4.worktalk.dto.pay;

import com.golfzonTech4.worktalk.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderSearch {

    private Integer pageNum; // 페이지 번호
    private PaymentStatus payStatus; // 결제 유형
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reserveDate; // 예약 날짜
    private Integer spaceType; // 공간 타입
    private String roomName; // 공간 이름
    
}
