package com.golfzonTech4.worktalk.dto.space;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SpaceSearchDto {

    private String searchDateType;

    private int searchSpaceType;

    private String searchBy;

    private String searchQuery = "";
}
