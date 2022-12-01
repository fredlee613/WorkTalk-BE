package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@SequenceGenerator(name = "SEQ_PENALTY_GENERATOR", sequenceName = "SEQ_PENALTY", initialValue = 1, allocationSize = 50)
@Getter
@Setter
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PENALTY_GENERATOR")
    @Column(name = "PENALTY_ID")
    private Long penaltyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "PENALTY_REASON", length = 1000, nullable = false)
    private String penaltyReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "PENALTY_TYPE", length = 50, nullable = false)
    private PenaltyType penaltyType;

    @Column(name = "PENALTY_DATE", nullable = false)
    private LocalDateTime penaltyDate;
}
