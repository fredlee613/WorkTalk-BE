package com.golfzonTech4.worktalk.dto.space;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Getter @Setter
@ToString
public class SpaceSearchDto {

    private Integer searchSpaceType;

    private String searchSpaceName;

    private String searchAddress;

    private LocalDate searchStartDate;

    private LocalDate searchEndDate;

    private Integer searchStartTime;

    private Integer searchEndTime;

    private int pageNum;
}
