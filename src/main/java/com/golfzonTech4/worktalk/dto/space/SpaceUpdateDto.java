package com.golfzonTech4.worktalk.dto.space;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter @Setter @NoArgsConstructor @ToString
public class SpaceUpdateDto {

    private Long memberId;

    private String name;

    private Long spaceId;

    private String spaceDetail;

    private String spaceImg;


}
