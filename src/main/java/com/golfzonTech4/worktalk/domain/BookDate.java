package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Embeddable
@Getter
@ToString
public class BookDate {

    private LocalDateTime reserveDate; // 예약 일자

    private LocalDateTime cancelDate; // 취소 일자

    private LocalDate CheckInDate; // 사용 시작 일자

    private LocalDate CheckOutDate; // 사용 종료 일자

    private Integer checkInTime; // 사용 시작 시간

    private Integer checkOutTime; // 사용 종료 시간

    public BookDate() {
    }

    public BookDate(LocalDateTime reserveDate, LocalDate checkInDate, LocalDate checkOutDate, Integer checkInTime, Integer checkOutTime) {
        this.reserveDate = reserveDate;
        CheckInDate = checkInDate;
        CheckOutDate = checkOutDate;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }

    public static int getPeriodDate(LocalDate checkInDate, LocalDate checkOutDate) {
        int period;
        if (checkOutDate.equals(checkOutDate)) { period = 1; }
        period = (int) ChronoUnit.DAYS.between(checkOutDate, checkInDate);
        System.out.println("period = " + period);
        return period;
    }

    public static int getPeriodHours(int checkInTime, int checkOutTime) {
        return checkOutTime - checkInTime;
    }
    public static int getPeriodMinutes(LocalDateTime reserveDate, LocalDateTime cancelDate) {
        return (int)ChronoUnit.MINUTES.between(reserveDate, cancelDate);
    }

    public static LocalDateTime getInitTime(LocalDate checkInDate, Integer checkInTime) {
        LocalDateTime initTime = checkInDate.atTime(checkInTime, 0);
        return initTime;
    }

}
