package com.golfzonTech4.worktalk.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "REVIEW_COMMENT")
public class ReviewComment implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "REVIEW_ID")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "REVIEWCOMMENT", nullable = false, length = 1000)
    private String reviewcomment;

    @Column(name = "REVIEWCOMMENT_DATE")
    private LocalDateTime reviewcomment_date;
}
