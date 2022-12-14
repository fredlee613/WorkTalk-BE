package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.dto.space.SpaceImgDto;
import com.golfzonTech4.worktalk.dto.space.SpaceInsertDto;
import com.golfzonTech4.worktalk.dto.space.SpaceManageSortingDto;
import com.golfzonTech4.worktalk.dto.space.SpaceUpdateDto;
import com.golfzonTech4.worktalk.service.SpaceImgService;
import com.golfzonTech4.worktalk.service.SpaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;
    private final SpaceImgService spaceImgService;

    //호스트가 등록한 사무공간리스트 조회
    @GetMapping("/host/spaceAll")
    public ResponseEntity findHostSpaces(){
        log.info("findHostSpaces :{}");
        return ResponseEntity.ok(spaceService.selectSpaceByHost());
    }

    //사무공간 상세페이지
    @GetMapping("/spaceOne/{spaceId}")
    public ResponseEntity findOneSpace(@PathVariable("spaceId") final Long spaceId){
        return ResponseEntity.ok(spaceService.getSpaceDetailPage(spaceId));
    }

    //호스트의 사무공간 등록
    @PostMapping("/host/spaceCreate")
    public ResponseEntity<Space> createSpace(@Valid SpaceInsertDto dto){
        log.info("createSpace : {}", dto);
        spaceService.createSpace(dto);

        return new ResponseEntity("입력완료",HttpStatus.OK);
    }

    //호스트 사무공간 수정(사무공간 설명, 이미지)
    @PostMapping("/host/space_update/{spaceId}")
    public ResponseEntity<Space> updateSpace(@Valid SpaceUpdateDto dto){
        spaceService.updateSpace(dto);
        return new ResponseEntity("수정완료",HttpStatus.OK);
    }

    //호스트의 사무공간삭제
    @DeleteMapping("/host/spaceDelete/{spaceId}")
    public ResponseEntity<Void> deleteSpace(@PathVariable Long spaceId){
        spaceService.deleteSpace(spaceId);
        return ResponseEntity.ok().build();
    }

    //마스터 사무공간 관리 페이지 - 사무공간 타입, 승인상태 소팅
    @GetMapping("/master/spaceAll")
    public ResponseEntity masterManageSpaces(@ModelAttribute SpaceManageSortingDto dto){
        PageRequest pageRequest = PageRequest.of(dto.getPageNum(), 10);
        return ResponseEntity.ok(spaceService.getSpaceMasterPage(pageRequest, dto));
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

    //사무공간 이미지 선택 삭제
    @PostMapping("/spaceImgDelete")
    public ResponseEntity<Space> spaceImgDelete(@ModelAttribute SpaceImgDto dto){
        log.info("spaceImgDelete : {}", dto);
        spaceImgService.deleteSpaceImg(dto);

        return new ResponseEntity("삭제완료",HttpStatus.OK);
    }

}
