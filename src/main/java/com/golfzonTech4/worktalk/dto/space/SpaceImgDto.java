package com.golfzonTech4.worktalk.dto.space;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter @NoArgsConstructor
public class SpaceImgDto {

    private Long spaceImgId;

    private String imgName;

    private Long spaceId;

}
