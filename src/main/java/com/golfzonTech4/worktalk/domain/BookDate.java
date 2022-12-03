package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Embeddable
@Getter
@ToString
public class BookDate {

    @Column(nullable = false)
    private LocalDateTime reserveDate; // 예약 일자
    private LocalDateTime cancelDate; // 취소 일자

    @Column(nullable = false)
    private LocalDate checkInDate; // 사용 시작 일자

    @Column(nullable = false)
    private LocalDate checkOutDate; // 사용 종료 일자

    @Column(nullable = false)
    private Integer checkInTime; // 사용 시작 시간

    @Column(nullable = false)
    private Integer checkOutTime; // 사용 종료 시간

    public BookDate() {
    }

    public BookDate(LocalDateTime reserveDate, LocalDate checkInDate, LocalDate checkOutDate, Integer checkInTime, Integer checkOutTime) {
        this.reserveDate = reserveDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }

    public static int getPeriodDate(LocalDate checkInDate, LocalDate checkOutDate) {
        int period;
        if (checkOutDate.equals(checkOutDate)) {
            period = 1;
        }
        period = (int) ChronoUnit.DAYS.between(checkOutDate, checkInDate);
        System.out.println("period = " + period);
        return period;
    }

    public static int getPeriodHours(int checkInTime, int checkOutTime) {
        return checkOutTime - checkInTime;
    }

    public static int getPeriodMinutes(LocalDateTime reserveDate, LocalDateTime cancelDate) {
        return (int) ChronoUnit.MINUTES.between(reserveDate, cancelDate);
    }

    public static int getPeriodSeconds(LocalDateTime reserveDate, LocalDateTime cancelDate) {
        return (int) ChronoUnit.SECONDS.between(cancelDate, reserveDate);
    }

    public static LocalDateTime getInitTime(LocalDate checkInDate, Integer checkInTime) {
        LocalDateTime initTime = checkInDate.atTime(checkInTime, 0);
        return initTime;
    }

    public static LocalDateTime getEndTime(LocalDate checkInDate, Integer checkOutTime) {
        LocalDateTime endTime = checkInDate.atTime(checkOutTime, 0);
        return endTime;
    }

    public static boolean validTime(int checkInTime, int checkOutTime) {
        if (checkInTime <= checkOutTime) return true;
        else return false;
    }

    public static boolean validDate(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate.isAfter(checkOutDate) || // 입실 날짜가 퇴실 날짜보다 늦은 경우
                checkInDate.isAfter(LocalDate.now()) // 입실 날짜가 예약일을 이미 지난 경우
        ) return false;
        else return true;
    }

}
