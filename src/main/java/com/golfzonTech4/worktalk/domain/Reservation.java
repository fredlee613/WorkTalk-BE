package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@SequenceGenerator(name = "SEQ_RESERVATION_GENERATOR", sequenceName = "SEQ_RESERVATION", initialValue = 1, allocationSize = 50)
@Getter @Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RESERVATION_GENERATOR")
    @Column(name = "RESERVATION_ID")
    private Long reserveId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID")
    private Room room;

    @Column(name = "CHECK_IN")
    private LocalDateTime checkIn;

    @Column(name = "CHECK_OUT")
    private LocalDateTime checkOut;

    @Enumerated(EnumType.STRING)
    @Column(name = "RESERVATION_STATUS", length = 20)
    private Reservation_status reserveStatus;

    @Column(name = "PAY_STATUS")
    private int payStatus;

    @Column(name = "RESERVATION_DATE")
    private LocalDateTime reserveDate;

    @Column(name = "AMOUNT")
    private int reserveAmount;

    @Column(name = "CANCEL_REASON", length = 500)
    private String cancelReason;

}
