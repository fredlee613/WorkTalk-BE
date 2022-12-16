package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "QNA_COMMENT")
@SequenceGenerator(name = "SEQ_QNA_COMMENT_GENERATOR", sequenceName = "SEQ_QNA_COMMENT", initialValue = 1, allocationSize = 50)
public class QnaComment extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_QNA_COMMENT_GENERATOR")
    @Column(name = "QNA_COMMENT_ID")
    private Long qnaCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QNA_ID", nullable = false)
    private Qna qna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "QNACOMMENT", nullable = false, length = 1000)
    private String qnacomment;

}
