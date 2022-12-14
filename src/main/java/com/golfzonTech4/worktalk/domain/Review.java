package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "SEQ_REVIEW_GENERATOR", sequenceName = "SEQ_REVIEW_QNA", initialValue = 1, allocationSize = 50)
public class Review extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REVIEW_GENERATOR")
    @Column(name = "REVIEW_ID")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESERVATION_ID", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "CONTENT", nullable = true, length = 1000)
    private String content;

    @Column(name = "GRADE", nullable = false)
    private Double grade;

}
