package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "SEQ_RESERVATION_GENERATOR", sequenceName = "SEQ_RESERVATION", initialValue = 1, allocationSize = 50)
@Getter @Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RESERVATION_GENERATOR")
    @Column(name = "RESERVATION_ID")
    private Long reserveId; // 예약 고유 번호

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member; // 예약 회원

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID")
    private Room room; // 예약 세부 공간

    @Enumerated(EnumType.STRING)
    @Column(name = "RESERVATION_STATUS", length = 20)
    private ReserveStatus reserveStatus; // 예약 상태 (BOOKED, CANCELED_BY_USER, CANCELED_BY_HOST, USED)

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS", length = 20)
    private PaymentStatus paymentStatus; // 결제 방법 (DEPOSIT, PREPAID, POSTPAID, REFUND)
    @Column(name = "PAID")
    private int paid; // 결제 상태 : 보증금 or 선결제 (결제: 0, 미결제: 1)


    @Embedded
    private BookDate bookDate;

    @Column(name = "AMOUNT")
    private int reserveAmount; // 예상 금액

    @Column(name = "CANCEL_REASON", length = 200)
    private String cancelReason; // 취소 사유

    @Override
    public String toString() {
        return "Reservation{" +
                "reserveId=" + reserveId +
                ", room=" + room +
                ", reserveStatus=" + reserveStatus +
                ", paid=" + paid +
                ", bookDate=" + bookDate +
                ", reserveAmount=" + reserveAmount +
                ", cancelReason='" + cancelReason + '\'' +
                '}';
    }

    public static Reservation makeReservation(Member member, Room room, BookDate bookDate, int amount, PaymentStatus paymentStatus) {
        Reservation reservation = new Reservation();
        reservation.setMember(member);
        reservation.setRoom(room);
        reservation.setReserveStatus(ReserveStatus.BOOKED);
        reservation.setPaymentStatus(paymentStatus);
        reservation.setPaid(0);
        reservation.setBookDate(bookDate);
        reservation.setReserveAmount(amount);
        return reservation;
    }

    private static int calAmount(Room room, BookDate bookDate) {
        int period;
        if (room.getRoomType() == RoomType.OFFICE)  period = BookDate.getPeriodDate(bookDate.getCheckOutDate(), bookDate.getCheckInDate());
        else  period = BookDate.getPeriodHours(bookDate.getCheckInTime(), bookDate.getCheckOutTime());

        return room.getRoomPrice() * period;
    }

}
