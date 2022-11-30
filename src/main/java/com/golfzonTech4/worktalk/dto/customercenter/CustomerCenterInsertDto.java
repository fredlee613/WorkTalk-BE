package com.golfzonTech4.worktalk.dto.customercenter;

import com.golfzonTech4.worktalk.domain.CcType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerCenterInsertDto {

    private Long ccId;

    private Long memberId;

    private String title;

    private String content;

    private CcType type;
}
