package com.golfzonTech4.worktalk.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

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

    @Column(name = "TEL", nullable = false, length = 20)
    private String tel;

    @Enumerated(EnumType.STRING)
    @Column(name = "MEMBER_TYPE")
    private MemberType memberType;

    private int penalty;

    @Column(columnDefinition = "VARCHAR(100 char) DEFAULT 'profill.png'")
    private String imgName;

    @JsonBackReference //순환참조를 방지하기 위한 어노테이션입니다
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Space> spaces;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", memberType=" + memberType +
                ", penalty=" + penalty +
                ", imgName='" + imgName + '\'' +
                '}';
    }
}
