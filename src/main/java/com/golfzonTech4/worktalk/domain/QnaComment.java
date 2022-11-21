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
@Table(name = "QNA_COMMENT")
public class QnaComment implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "QNA_ID")
    private Qna qna;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "QNACOMMENT", nullable = false, length = 1000)
    private String qnacomment;

    @Enumerated(EnumType.STRING)
    private CcType type;

    @Column(name = "QNACOMMENT_DATE")
    private LocalDateTime qnacomment_date;


}
