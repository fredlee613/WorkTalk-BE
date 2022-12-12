package com.golfzonTech4.worktalk.dto.space;

import com.golfzonTech4.worktalk.dto.qna.QnaDetailDto;
import com.golfzonTech4.worktalk.dto.review.ReviewDetailDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class SpaceDetailDto {

    private Long memberId;

    private Long spaceId;

    private String name;

    private String email;

    private String spaceName;

    private String spaceDetail;

    private String postcode;

    private String address;

    private String detailAddress;

    private String regCode;

    private int spaceType;

    private List<SpaceImgDto> spaceImgList = new ArrayList<>();

    private List<QnaDetailDto> qnaDetailDtoList = new ArrayList<>();

    private List<ReviewDetailDto> reviewDetailDtoList = new ArrayList<>();

    @QueryProjection
    public SpaceDetailDto(Long memberId, Long spaceId, String name, String email, String spaceName, String spaceDetail,
                          String postcode, String address, String detailAddress, String regCode, int spaceType) {
        this.memberId = memberId;
        this.spaceId = spaceId;
        this.name = name;
        this.email = email;
        this.spaceName = spaceName;
        this.spaceDetail = spaceDetail;
        this.postcode = postcode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.regCode = regCode;
        this.spaceType = spaceType;
    }
}
