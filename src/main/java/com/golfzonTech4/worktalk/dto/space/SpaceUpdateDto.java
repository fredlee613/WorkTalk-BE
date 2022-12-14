package com.golfzonTech4.worktalk.dto.space;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter @NoArgsConstructor @ToString
public class SpaceUpdateDto {

    private Long spaceId;

    private String spaceDetail;

    List<MultipartFile> multipartFileList;

}
