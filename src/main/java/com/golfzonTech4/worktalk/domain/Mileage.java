package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@SequenceGenerator(name = "SEQ_MILEAGE_GENERATOR", sequenceName = "SEQ_MILEAGE", initialValue = 1, allocationSize = 50)
@Getter
@Setter
public class Mileage implements Serializable {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAY_ID")
    private Pay pay;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Mileage_status status;

    @Column(name = "MILEAGE_AMOUNT")
    private int mileageAmount;

    @Column(name = "MILEAGE_DATE")
    private LocalDateTime mileageDate;
}
