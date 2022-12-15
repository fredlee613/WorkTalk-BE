package com.golfzonTech4.worktalk.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

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

    @Column(name = "PW", nullable = false, length = 100)
    private String pw;

    @Column(name = "NAME", unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "TEL", nullable = true, length = 20)
    private String tel;

    @Enumerated(EnumType.STRING)
    @Column(name = "MEMBER_TYPE", nullable = false)
    private MemberType memberType;

    @ColumnDefault("'profill.png'")
    @Column(name = "IMG_NAME", length = 50)
    private String imgName;

    @ColumnDefault("1")
    @Column(name = "ACTIVATED", nullable = false)
    private int activated;

    @ColumnDefault("'N'")
    @Column(name = "KAKAO_YN", nullable = true, length = 10)
    private String KakaoYn;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Space> spaces;

}
