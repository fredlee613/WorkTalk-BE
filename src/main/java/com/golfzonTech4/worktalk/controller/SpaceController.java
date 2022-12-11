package com.golfzonTech4.worktalk.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.dto.space.SpaceInsertDto;
import com.golfzonTech4.worktalk.service.SpaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    private final AmazonS3 amazonS3;
//    private String S3Bucket = "worktalk-img";


    //호스트가 등록한 사무공간리스트 조회
    @GetMapping("/host/spaceAll/{name}")
    public ResponseEntity findHostSpaces(@PathVariable("name") String name){
        return ResponseEntity.ok(spaceService.selectSpaceByHost(name));
    }

    //사무공간 상세페이지
    @GetMapping("/spaceOne/{spaceId}")
    public ResponseEntity findOneSpace(@PathVariable("spaceId") final Long spaceId){
        return ResponseEntity.ok(spaceService.selectSpace(spaceId));
    }

    //호스트의 사무공간 등록
    @PostMapping("/host/spaceCreate")
    public ResponseEntity<Space> createSpace(@Valid SpaceInsertDto dto){
        log.info("createSpace : {}", dto);
        spaceService.createSpace(dto);

        return new ResponseEntity("입력완료",HttpStatus.OK);
    }

    //호스트 사무공간 수정(사무공간 설명, 이미지)
//    @PostMapping("/host/space_update/{spaceId}")
//    public ResponseEntity<Space> updateSpace(@Valid SpaceUpdateDto dto, BindingResult result){
//        spaceService.updateSpace(dto);
//        return new ResponseEntity("수정완료",HttpStatus.OK);
//    }

    //호스트의 사무공간삭제
    @DeleteMapping("/host/spaceDelete/{spaceId}")
    public ResponseEntity<Void> deleteSpace(@PathVariable Long spaceId){
        spaceService.deleteSpace(spaceId);
        return ResponseEntity.ok().build();
    }

    //마스터의 사무공간 승인
    @PostMapping("/spaceApproved/{spaceId}")
    public ResponseEntity spaceApproved(@PathVariable("spaceId") final Long spaceId){
        spaceService.ApprovedSpace(spaceId);
        return new ResponseEntity("승인완료하였습니다",HttpStatus.OK);
    }

    //마스터의 사무공간 승인거절
    @PostMapping("/spaceRejected/{spaceId}")
    public ResponseEntity spaceRejected(@PathVariable("spaceId") final Long spaceId){
        spaceService.RejectedSpace(spaceId);
        return new ResponseEntity("승인거절하였습니다",HttpStatus.OK);
    }

    //이미지 업로드 테스트
//    @PostMapping("/upload")
//    public ResponseEntity<Object> upload(
//            List<MultipartFile> multipartFileList) throws Exception {
//        List<String> imagePathList = new ArrayList<>();
//        if(multipartFileList.size()>0) {
//            for (MultipartFile multipartFile : multipartFileList) {
//                String originalName = multipartFile.getOriginalFilename(); // 파일 이름
//                long size = multipartFile.getSize(); // 파일 크기
//
//                ObjectMetadata objectMetaData = new ObjectMetadata();
//                objectMetaData.setContentType(multipartFile.getContentType());
//                objectMetaData.setContentLength(size);
//
//                // S3에 업로드
//                amazonS3.putObject(
//                        new PutObjectRequest(S3Bucket, originalName, multipartFile.getInputStream(), objectMetaData)
//                                .withCannedAcl(CannedAccessControlList.PublicRead)
//                );
//
//                String imagePath = amazonS3.getUrl(S3Bucket, originalName).toString(); // 접근가능한 URL 가져오기
//                imagePathList.add(imagePath);
//            }
//        }
//        return new ResponseEntity<Object>(imagePathList, HttpStatus.OK);
//    }


}
