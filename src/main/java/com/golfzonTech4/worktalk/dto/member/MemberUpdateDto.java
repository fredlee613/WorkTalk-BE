package com.golfzonTech4.worktalk.dto.member;

import com.golfzonTech4.worktalk.domain.MemberType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberUpdateDto {

    private Long id;
    private String pw;
    private String tel;

}
