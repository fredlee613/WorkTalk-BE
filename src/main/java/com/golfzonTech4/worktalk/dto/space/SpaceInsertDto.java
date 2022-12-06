package com.golfzonTech4.worktalk.dto.space;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Getter @Setter @NoArgsConstructor
public class SpaceInsertDto {

    private Long memberId;

    private String name;

    private Long spaceId;

    @NotEmpty(message = "사무공간명을 입력해주세요")
    private String spaceName;

    private String spaceDetail;

    @NotEmpty(message = "우편번호를 입력해주세요")
    private String postcode;

    @NotEmpty(message = "주소를 입력해주세요")
    private String address;

    @NotEmpty(message = "상세주소를 입력해주세요")
    private String detailAddress;

    @NotEmpty(message = "사업자번호를 입력해주세요")
    private String regCode;

    private int spaceType;

    MultipartFile multipartFile;

}
