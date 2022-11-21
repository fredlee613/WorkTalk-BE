package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "SEQ_QNA_GENERATOR", sequenceName = "SEQ_QNA", initialValue = 1, allocationSize = 50)
public class Qna implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_QNA_GENERATOR")
    @Column(name = "QNA_ID")
    private Long qna_id;

    @ManyToOne
    @JoinColumn(name = "SPACE_ID", nullable = false)
    private com.golfzonTech4.worktalk.domain.Space space;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private com.golfzonTech4.worktalk.domain.QnaType type;

    @Column(name = "CONTENT", nullable = false, length = 1000)
    private String content;

    @Column(name = "qna_date")
    private LocalDateTime qna_date;
}
