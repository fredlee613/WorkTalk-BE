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
@Table(name = "CUSTOMER_COMMENT")
public class CustomerComment extends BaseTimeEntity implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "CC_ID")
    private CustomerCenter customerCenter;

    @Column(name = "CONTENT", nullable = true, length = 2000)
    private String content;

//    @Column(name = "cc_date")
//    private LocalDateTime cc_date;
}
