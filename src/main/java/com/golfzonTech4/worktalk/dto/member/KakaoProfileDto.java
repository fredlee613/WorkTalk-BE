package com.golfzonTech4.worktalk.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoProfileDto {

    private String name;

    private String email;

    private String pw;

    private boolean member;
}
