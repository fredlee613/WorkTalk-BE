package com.golfzonTech4.worktalk.dto.space;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter
@ToString
public class SpaceSearchDto {

    private Integer searchSpaceType;

    private String searchSpaceName;

    private String searchAddress;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchEndDate;

    private Integer searchStartTime;

    private Integer searchEndTime;

    private int pageNum;
}
