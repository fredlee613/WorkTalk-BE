package com.golfzonTech4.worktalk.dto.space;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter
@ToString
public class SpaceManageSortingDto {

    private Integer searchSpaceType;

    private String searchSpaceStatus;

    private int pageNum;
}
