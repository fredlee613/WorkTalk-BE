package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Room;
import com.golfzonTech4.worktalk.dto.room.RoomImgDto;
import com.golfzonTech4.worktalk.dto.room.RoomInsertDto;
import com.golfzonTech4.worktalk.dto.room.RoomUpdateDto;
import com.golfzonTech4.worktalk.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    //사무공간상세페이지에서 세부공간 리스트 출력
    @GetMapping("/spaceOne/{spaceId}/rooms")
    public ResponseEntity findByRooms(@PathVariable("spaceId") Long spaceId){
        log.info("spaceId : {}", spaceId);
        return ResponseEntity.ok(roomService.getRooms(spaceId));
    }

    //호스트의 세부사무공간 등록
    @PostMapping("/host/roomCreate")
    public ResponseEntity<Room> createRoom(@Valid RoomInsertDto dto){
        log.info("createRoom : {}", dto);
        roomService.createRoom(dto);

        return new ResponseEntity("입력완료", HttpStatus.OK);
    }

    //호스트 세부사무공간 수정(공간 설명, 이미지, 영업시간, 가격)
    @PostMapping("/host/roomUpdate/{roomId}")
    public ResponseEntity<Room> updateRoom(@Valid RoomUpdateDto dto){
        roomService.updateRoom(dto);
        return new ResponseEntity("수정완료",HttpStatus.OK);
    }

    //사무공간 상세페이지에서 세부공간 선택
    @GetMapping("/roomOne/{roomId}")
    public ResponseEntity findOneSpace(@PathVariable("roomId") final Long roomId){
        return ResponseEntity.ok(roomService.selectRoom(roomId));
    }

    //세부공간 삭제
    @DeleteMapping("/host/spaceDelete/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId){
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }

    //세부공간 이미지 선택 삭제
    @PostMapping("/roomImgDelete")
    public ResponseEntity<Room> roomImgDelete(@ModelAttribute RoomImgDto dto){
        log.info("roomImgDelete : {}", dto);
        roomService.deleteRoomImg(dto);

        return new ResponseEntity("삭제완료",HttpStatus.OK);
    }

}
