package com.golfzonTech4.worktalk.dto.space;

import com.golfzonTech4.worktalk.domain.SpaceImg;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SpaceMainDto {

    private Long spaceId;

    private String spaceName;

    private String address;

    private String detailAddress;

    private int spaceType;

    private List<SpaceImgDto> spaceImgList = new ArrayList<>();

    private Double gradeAvg;

    private int count;

    @QueryProjection
    public SpaceMainDto(Long spaceId, String spaceName, String address, String detailAddress, int spaceType) {
        this.spaceId = spaceId;
        this.spaceName = spaceName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.spaceType = spaceType;
    }

    @QueryProjection
    public SpaceMainDto(Long spaceId, String spaceName, String address, String detailAddress,
                        int spaceType, List<SpaceImgDto> spaceImgList, Double gradeAvg, int count) {
        this.spaceId = spaceId;
        this.spaceName = spaceName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.spaceType = spaceType;
        this.spaceImgList = spaceImgList;
        this.gradeAvg = gradeAvg;
        this.count = count;
    }
}
