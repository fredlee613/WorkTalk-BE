package com.golfzonTech4.worktalk.dto.space;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @NoArgsConstructor
public class SpaceImgDto {

    private Long spaceImgId;

    private String imgName;

    private MultipartFile multipartFile;

    private Long spaceId;

}
