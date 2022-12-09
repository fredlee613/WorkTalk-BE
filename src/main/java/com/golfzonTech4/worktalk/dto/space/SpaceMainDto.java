package com.golfzonTech4.worktalk.dto.space;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpaceMainDto {

    private Long spaceId;

    private String spaceName;

    private String address;

    private int spaceType;

//    private String spaceImg;
//    private List<Review> reviewList;

    private Double gradeAvg;

    private int count;

    @QueryProjection
    public SpaceMainDto(Long spaceId, String spaceName, String address, int spaceType) {
        this.spaceId = spaceId;
        this.spaceName = spaceName;
        this.address = address;
        this.spaceType = spaceType;
    }



    @QueryProjection
    public SpaceMainDto(Long spaceId, String spaceName, String address, int spaceType, Double gradeAvg, int count) {
        this.spaceId = spaceId;
        this.spaceName = spaceName;
        this.address = address;
        this.spaceType = spaceType;
        this.gradeAvg = gradeAvg;
        this.count = count;
    }
}
