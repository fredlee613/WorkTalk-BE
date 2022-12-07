package com.golfzonTech4.worktalk.dto.space;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SpaceMainDto {

    private Long spaceId;

    private String spaceName;

    private String address;

    private int spaceType;

//    private String spaceImg;
    private Double gradeSum;

    private int count;

    @QueryProjection
    public SpaceMainDto(Long spaceId, String spaceName, String address, int spaceType) {
        this.spaceId = spaceId;
        this.spaceName = spaceName;
        this.address = address;
        this.spaceType = spaceType;
    }

    @QueryProjection
    public SpaceMainDto(Long spaceId, String spaceName, String address, int spaceType, Double gradeSum, int count) {
        this.spaceId = spaceId;
        this.spaceName = spaceName;
        this.address = address;
        this.spaceType = spaceType;
        this.gradeSum = gradeSum;
        this.count = count;
    }
}
