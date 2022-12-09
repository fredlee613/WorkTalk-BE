package com.golfzonTech4.worktalk.dto.space;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @NoArgsConstructor
public class SpaceImgDto {

    private Long spaceImgId;

    private String imgUrl;

    private MultipartFile multipartFile;

    private Long spaceId;

    public SpaceImgDto(Long spaceImgId, String imgUrl, MultipartFile multipartFile, Long spaceId) {
        this.spaceImgId = spaceImgId;
        this.imgUrl = imgUrl;
        this.multipartFile = multipartFile;
        this.spaceId = spaceId;
    }
}
