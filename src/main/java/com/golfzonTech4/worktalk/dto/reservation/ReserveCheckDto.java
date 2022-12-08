package com.golfzonTech4.worktalk.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.golfzonTech4.worktalk.domain.RoomType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ReserveCheckDto {

    private Long roomId;
    private RoomType roomType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate initDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;
    private int initTime;
    private int endTime;

    @QueryProjection
    public ReserveCheckDto(Long roomId, LocalDate initDate, LocalDate endDate, int initTime, int endTime) {
        this.roomId = roomId;
        this.initDate = initDate;
        this.endDate = endDate;
        this.initTime = initTime;
        this.endTime = endTime;
    }
}
