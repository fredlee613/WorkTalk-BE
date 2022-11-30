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
    @Column(name = "QNA_ID")
    private Long qnaId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId//@매핑한 연관관계를 기본키에도 매핑하기 위함
    @JoinColumn(name = "QNA_ID")
    private Qna qna;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "QNACOMMENT", nullable = false, length = 1000)
    private String qnacomment;

}
