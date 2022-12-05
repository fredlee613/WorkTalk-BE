package com.golfzonTech4.worktalk.dto.space;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class SpaceMainDto {

    private Long spaceId;

    private String spaceName;

    private String address;

    private int spaceType;

    private String spaceImg;

    @QueryProjection
    public SpaceMainDto(Long spaceId, String spaceName, String address, int spaceType, String spaceImg) {
        this.spaceId = spaceId;
        this.spaceName = spaceName;
        this.address = address;
        this.spaceType = spaceType;
        this.spaceImg = spaceImg;
    }
}
