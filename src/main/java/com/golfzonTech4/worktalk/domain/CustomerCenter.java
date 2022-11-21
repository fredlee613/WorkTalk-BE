package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "SEQ_CUSTOMER_CENTER_GENERATOR", sequenceName = "SEQ_CUSTOMER_CENTER", initialValue = 1, allocationSize = 50)
@Table(name = "CUSTOMER_CENTER")
public class CustomerCenter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CUSTOMER_CENTER_GENERATOR")
    @Column(name = "CC_ID")
    private Long cc_id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "TITLE", nullable = true, length = 50)
    private String title;

    @Column(name = "CONTENT", nullable = true, length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    private com.golfzonTech4.worktalk.domain.CcType type;

    @Column(name = "cc_date")
    private LocalDateTime cc_date;
}
