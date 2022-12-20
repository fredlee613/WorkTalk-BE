package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Embedded;

@RedisHash(value = "temp")
@Getter @Setter
public class TempRedisReservation {

    public static final Long DEFAULT_TTL = 300L;

    @Id
    private String tempReserveId; // 예약 고유 번호
    private Long memberId; // 예약 회원
    @Indexed
    private Long roomId; // 예약 세부 공간
    @Embedded
    private BookDate bookDate;

    @TimeToLive
    private Long expiration = DEFAULT_TTL;

    @Override
    public String toString() {
        return "TempRedisReservation{" +
                "tempReserveId=" + tempReserveId +
                ", memberId=" + memberId +
                ", roomId=" + roomId +
                ", bookDate=" + bookDate +
                '}';
    }

    public static TempRedisReservation makeTempReservation(Long memberId, Long roomId, BookDate bookDate) {
        TempRedisReservation reservation = new TempRedisReservation();
        reservation.setMemberId(memberId);
        reservation.setRoomId(roomId);
        reservation.setBookDate(bookDate);
        return reservation;
    }

    private static int calAmount(Room room, BookDate bookDate) {
        int period;
        if (room.getRoomType() == RoomType.OFFICE)  period = BookDate.getPeriodDate(bookDate.getCheckOutDate(), bookDate.getCheckInDate());
        else  period = BookDate.getPeriodHours(bookDate.getCheckInTime(), bookDate.getCheckOutTime());

        return room.getRoomPrice() * period;
    }

}
