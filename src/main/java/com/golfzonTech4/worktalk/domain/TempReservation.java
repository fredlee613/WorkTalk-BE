package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "SEQ_TEMP_RESERVATION_GENERATOR", sequenceName = "SEQ_TEMP_RESERVATION", initialValue = 1, allocationSize = 50)
@Getter @Setter
public class TempReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TEMP_RESERVATION_GENERATOR")
    @Column(name = "RESERVATION_ID")
    private Long tempReserveId; // 예약 고유 번호

    private Long memberId; // 예약 회원

    private Long roomId; // 예약 세부 공간

    @Embedded
    private BookDate bookDate;

    @Override
    public String toString() {
        return "TempReservation{" +
                "tempReserveId=" + tempReserveId +
                ", memberId=" + memberId +
                ", roomId=" + roomId +
                ", bookDate=" + bookDate +
                '}';
    }

    public static TempReservation makeTempReservation(Long memberId, Long roomId, BookDate bookDate) {
        TempReservation reservation = new TempReservation();
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
