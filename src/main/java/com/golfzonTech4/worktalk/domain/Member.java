package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "SEQ_MEMBER_GENERATOR", sequenceName = "SEQ_MEMBER", initialValue = 1, allocationSize = 50)
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MEMBER_GENERATOR")
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "EMAIL", unique = true, nullable = false, length = 50)
    private String email;

    @Column(name = "PW", nullable = false, length = 50)
    private String pw;

    @Column(name = "NAME", unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "TEL", nullable = false, length = 20)
    private String tel;

    @Enumerated(EnumType.STRING)
    @Column(name = "MEMBER_TYPE")
    private MemberType memberType;

    private int penalty;

    @Column(columnDefinition = "VARCHAR(100 char) DEFAULT 'profill.png'")
    private String imgname;
}
