package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Room;
import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.dto.room.RoomInsertDto;
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
    public ResponseEntity<Room> createRoom(@Valid RoomInsertDto dto, Space space){
        log.info("createRoom : {}", dto);
        roomService.createRoom(dto);

        return new ResponseEntity("입력완료", HttpStatus.OK);
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

}
