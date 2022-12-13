package com.golfzonTech4.worktalk.dto.space;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SpaceMasterDto {

    private Long spaceId;

    private String hostName;

    private String spaceName;

    private int spaceType;

    private String regCode;

    private String spaceStatus;

    @QueryProjection
    public SpaceMasterDto(Long spaceId, String hostName, String spaceName, int spaceType, String regCode, String spaceStatus) {
        this.spaceId = spaceId;
        this.hostName = hostName;
        this.spaceName = spaceName;
        this.spaceType = spaceType;
        this.regCode = regCode;
        this.spaceStatus = spaceStatus;
    }
}
