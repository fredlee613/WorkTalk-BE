package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.dto.space.SpaceUpdateDto;
import com.golfzonTech4.worktalk.service.RoomService;
import com.golfzonTech4.worktalk.service.SpaceService;
import com.golfzonTech4.worktalk.dto.space.SpaceInsertDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;
    private final RoomService roomService;

    //유저-사무공간 전체리스트 조회
    @GetMapping("/user/spaceAll")
    public ResponseEntity findSpaces(){
        return ResponseEntity.ok(spaceService.selectSpaceAll());
    }

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
    public ResponseEntity<Space> createSpace(@Valid @RequestBody SpaceInsertDto dto/*, BindingResult result*/){
//        if(result.hasErrors()){
//            return new ResponseEntity("null값이 있습니다", HttpStatus.BAD_REQUEST) ;
//        }
        spaceService.createSpace(dto);

//        return new ResponseEntity.ok(spaceService.createSpace(form));
        return new ResponseEntity("입력완료",HttpStatus.OK);
    }

    //호스트의 사무공간 등록-다중이미지
    @PostMapping("/host/space_createi")
    public ResponseEntity<Space> createiSpace(
            @Valid @RequestBody SpaceInsertDto dto, @RequestParam("SpaceImgFile") List<MultipartFile> multipartFileList, MultipartHttpServletRequest req /*, BindingResult result*/) {
//        if(result.hasErrors()){
//            return new ResponseEntity("null값이 있습니다", HttpStatus.BAD_REQUEST) ;
//        }
        try {
            spaceService.createiSpace(dto,multipartFileList);
        } catch (Exception e){
            log.info("등록 중 에러 발생");
        }

//        return new ResponseEntity.ok(spaceService.createSpace(form));
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
        return new ResponseEntity("승인완료",HttpStatus.OK);
    }

}
