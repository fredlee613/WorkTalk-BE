package com.golfzonTech4.worktalk.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.golfzonTech4.worktalk.domain.BookDate;
import com.golfzonTech4.worktalk.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReserveTempDto {

    private Long tempReserveId; // 예약 고유 번호

    private Long memberId; // 예약 회원

    private Long roomId; // 예약 세부 공간

    private LocalDateTime reserveDate; // 예약 시간

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate checkInDate; // 사용 시작 일자

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate checkOutDate; // 사용 종료 일자

    private Integer checkInTime; // 사용 시작 시간

    private Integer checkOutTime; // 사용 종료 시간
}
