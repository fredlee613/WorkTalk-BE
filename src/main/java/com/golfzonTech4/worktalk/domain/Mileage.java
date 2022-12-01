package com.golfzonTech4.worktalk.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@SequenceGenerator(name = "SEQ_MILEAGE_GENERATOR", sequenceName = "SEQ_MILEAGE", initialValue = 1, allocationSize = 50)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mileage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MILEAGE_GENERATOR")
    @Column(name = "MILEAGE_ID")
    private Long mileageId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAY_ID")
    private Pay pay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Mileage_status status;

    @Column(name = "MILEAGE_AMOUNT")
    private int mileageAmount;

    @Column(name = "MILEAGE_DATE")
    private LocalDate mileageDate;
}
