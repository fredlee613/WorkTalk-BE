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

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;
    private final RoomService roomService;

    //유저-사무공간 전체리스트 조회
    @GetMapping("/user/space_selectAll")
    public ResponseEntity findSpaces(){
        return ResponseEntity.ok(spaceService.selectSpaceAll());
    }

    //호스트가 등록한 사무공간리스트 조회
    @GetMapping("/host/space_selectAll/{member}")
    public ResponseEntity findHostSpaces(@PathVariable("member") Member member){
        return ResponseEntity.ok(spaceService.selectSpaceByHost(member));
    }

    //사무공간 상세페이지
    @GetMapping("/space_selectOne/{spaceId}")
    public ResponseEntity findOneSpace(@PathVariable("spaceId") final Long spaceId){
        return ResponseEntity.ok(spaceService.selectSpace(spaceId));
    }

    //호스트의 사무공간 등록
    @PostMapping("/host/space_create")
    public ResponseEntity<Space> createSpace(@Valid @RequestBody SpaceInsertDto dto/*, BindingResult result*/){
//        if(result.hasErrors()){
//            return new ResponseEntity("null값이 있습니다", HttpStatus.BAD_REQUEST) ;
//        }
        spaceService.createSpace(dto);

//        return new ResponseEntity.ok(spaceService.createSpace(form));
        return new ResponseEntity("입력완료",HttpStatus.OK);
    }

    //호스트 사무공간 수정(사무공간 설명, 이미지)
//    @PostMapping("/host/space_update/{spaceId}")
//    public ResponseEntity<Space> updateSpace(@Valid SpaceUpdateDto dto, BindingResult result){
//        spaceService.updateSpace(dto);
//        return new ResponseEntity("수정완료",HttpStatus.OK);
//    }

    @DeleteMapping("/host/space_delete/{spaceId}")
    public ResponseEntity<Void> deleteSpace(@PathVariable Long spaceId){
        spaceService.deleteSpace(spaceId);
        return ResponseEntity.ok().build();
    }


}
