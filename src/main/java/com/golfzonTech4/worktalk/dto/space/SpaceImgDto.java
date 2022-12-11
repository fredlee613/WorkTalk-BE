package com.golfzonTech4.worktalk.dto.space;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class SpaceImgDto {

    private Long spaceImgId;

    private String spaceImgUrl;

    private Long spaceId;

    @QueryProjection
    public SpaceImgDto(Long spaceImgId, String spaceImgUrl, Long spaceId) {
        this.spaceImgId = spaceImgId;
        this.spaceImgUrl = spaceImgUrl;
        this.spaceId = spaceId;
    }
}
