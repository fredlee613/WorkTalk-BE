package com.golfzonTech4.worktalk.dto.room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter @NoArgsConstructor @ToString
public class RoomUpdateDto {

    private Long spaceId;

    private Long roomId;

    private String roomDetail;

    private int roomPrice;

    private int workStart;

    private int workEnd;

    List<String> imageUrlList;

    List<MultipartFile> multipartFileList;

    private String offeringOption;
}
