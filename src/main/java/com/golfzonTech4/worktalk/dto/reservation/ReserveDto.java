package com.golfzonTech4.worktalk.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.golfzonTech4.worktalk.domain.PaymentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ReserveDto {
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
//    private LocalDateTime reserveDate; // 예약 일자

    private Long room_id;

    private Long reserveId;

    private String tempId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate CheckInDate; // 사용 시작 일자

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate CheckOutDate; // 사용 종료 일자

    private Integer checkInTime; // 사용 시작 시간

    private Integer checkOutTime; // 사용 종료 시간
    
    private String cancelReason; // 취소 사유

    private Integer amount; // 결제 가격

    private PaymentStatus paymentStatus; // 결제 유형

    private Long payId;

    public ReserveDto(Long room_id, String tempId, LocalDate checkInDate, LocalDate checkOutDate, Integer checkInTime, Integer checkOutTime) {
        this.room_id = room_id;
        this.tempId = tempId;
        CheckInDate = checkInDate;
        CheckOutDate = checkOutDate;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }
}
