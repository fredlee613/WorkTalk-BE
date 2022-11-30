package com.golfzonTech4.worktalk.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "CUSTOMER_COMMENT")
public class CustomerComment extends BaseTimeEntity implements Serializable {

    @Id
    @Column(name = "CC_ID")
    private Long ccId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId//@매핑한 연관관계를 기본키에도 매핑하기 위함
    @JoinColumn(name = "CC_ID")
    private CustomerCenter customerCenter;

    @Column(name = "CONTENT", nullable = false, length = 2000)
    private String content;

}
