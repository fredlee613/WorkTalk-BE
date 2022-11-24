package com.golfzonTech4.worktalk.dto.member;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotEmpty
    private String email;
    @NotEmpty
    private String pw;

}
