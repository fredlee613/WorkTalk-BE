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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAY_ID", nullable = false)
    private Pay pay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Mileage_status status;

    @Column(name = "MILEAGE_AMOUNT", nullable = false)
    private int mileageAmount;

    @Column(name = "MILEAGE_DATE", nullable = false)
    private LocalDate mileageDate;
}
