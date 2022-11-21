package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PAY")
@SequenceGenerator(name = "SEQ_PAY_GENERATOR", sequenceName = "SEQ_PAY", initialValue = 1, allocationSize = 50)
@Getter @Setter
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PAY_GENERATOR")
    @Column(name = "PAY_ID")
    private Long payId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESERVATION_ID")
    private Reservation reservation;

    @Column(name = "PAY_UID", length = 50)
    private String payUid;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAY_STATUS", length = 50)
    private Payment_status payStatus;

    @Column(name = "AMOUNT")
    private int payAmount;

    @Column(name = "PAY_DATE")
    private LocalDateTime payDate;
}
