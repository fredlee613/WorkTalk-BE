package com.golfzonTech4.worktalk.dto.space;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class SpaceSearchDto {

    private Integer searchSpaceType;

    private String searchSpaceName;

    private String searchAddress;

    private int pageNum;
}
