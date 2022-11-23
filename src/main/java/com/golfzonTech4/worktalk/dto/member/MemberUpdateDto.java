package com.golfzonTech4.worktalk.dto.member;

import com.golfzonTech4.worktalk.domain.MemberType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberUpdateDto {

    @NotEmpty
    private Long id;
    @NotEmpty
    private String email;
    @NotEmpty
    private String pw;
    @NotEmpty
    private String tel;

}
