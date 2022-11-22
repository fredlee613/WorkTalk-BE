package com.golfzonTech4.worktalk.dto.member;

import com.golfzonTech4.worktalk.domain.MemberType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberDetailDto {

    private Long id;
    @NotEmpty
    private String email;
    @NotEmpty
    private String pw;
    @NotEmpty
    private String name;
    @NotEmpty
    private String tel;
    private MemberType memberType;
    private int penalty;
    private String imgName;

}
