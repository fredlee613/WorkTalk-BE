package com.golfzonTech4.worktalk.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "PAY")
@SequenceGenerator(name = "SEQ_PAY_GENERATOR", sequenceName = "SEQ_PAY", initialValue = 1, allocationSize = 50)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PAY_GENERATOR")
    @Column(name = "PAY_ID")
    private Long payId; // 결제 데이터 고유 키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESERVATION_ID", nullable = false)
    private Reservation reservation; // 해당 결제가 이루어진 예약건

    @Column(name = "IMP_UID", length = 50)
    private String impUid; // 아임포트 결제 거래 번호

    @Column(name = "MERCHANT_UID", length = 50)
    private String merchantUid; // 가맹점 고유 번호

    @Column(name = "CUSTOMER_UID", length = 50)
    private String customerUid; // 개인 빌링키

    @Enumerated(EnumType.STRING)
    @Column(name = "PAY_STATUS", length = 50, nullable = false)
    private PaymentStatus payStatus; // 결제 유형 (DEPOSIT, PREPAID, POSTPAID, REFUND)

    @Column(name = "AMOUNT", nullable = false)
    private int payAmount; // 결제 금액

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "pay")
    private List<Mileage> mileages;
}
