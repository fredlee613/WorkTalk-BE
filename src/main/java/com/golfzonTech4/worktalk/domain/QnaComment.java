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
public class QnaComment extends BaseTimeEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CC_COMMENT_GENERATOR")
    @Column(name = "QNA_COMMENT_ID")
    private Long qnaCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QNA_ID", nullable = false)
    private Qna qna;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "QNACOMMENT", nullable = false, length = 1000)
    private String qnacomment;

}
