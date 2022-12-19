package com.golfzonTech4.worktalk.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoUserInfoDto {
    private Long id;
    private String email;
    private String nickname;
}
