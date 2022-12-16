package com.golfzonTech4.worktalk.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "SEQ_CC_COMMENT_GENERATOR", sequenceName = "SEQ_CC_COMMENT", initialValue = 1, allocationSize = 50)
@Table(name = "CUSTOMER_COMMENT")
public class CustomerComment extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CC_COMMENT_GENERATOR")
    @Column(name = "CC_COMMENT_ID")
    private Long ccCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CC_ID", nullable = false)
    private CustomerCenter customerCenter;

    @Column(name = "CONTENT", nullable = false, length = 2000)
    private String content;

}
