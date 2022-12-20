package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Room;
import com.golfzonTech4.worktalk.dto.room.RoomImgDeleteDto;
import com.golfzonTech4.worktalk.dto.room.RoomInsertDto;
import com.golfzonTech4.worktalk.dto.room.RoomUpdateDto;
import com.golfzonTech4.worktalk.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "RoomController", description = "세부 사무공간 api입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    //사무공간상세페이지에서 세부공간 리스트 출력
    @Operation(summary = "세부사무공간 리스트 조회 요청", description = "사무공간 상세 페이지에서 세부공간 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/spaceOne/{spaceId}/rooms")
    public ResponseEntity findByRooms(@PathVariable("spaceId") Long spaceId) {
        log.info("spaceId : {}", spaceId);
        return ResponseEntity.ok(roomService.getRooms(spaceId));
    }

    //호스트의 세부사무공간 등록
    @Operation(summary = "세부 사무공간 등록", description = "백오피스페이지에서 세부 사무공간을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/host/roomCreate")
    public ResponseEntity<Room> createRoom(@Valid RoomInsertDto dto) {
        log.info("createRoom : {}", dto);
        roomService.createRoom(dto);

        return new ResponseEntity("입력완료", HttpStatus.OK);
    }

    //호스트 세부사무공간 수정(공간 설명, 이미지, 영업시간, 가격)
    @Operation(summary = "세부 사무공간 수정", description = "백오피스페이지에서 세부 사무공간을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/host/roomUpdate/{roomId}")
    public ResponseEntity<Room> updateRoom(@Valid RoomUpdateDto dto) {
        roomService.updateRoom(dto);
        return new ResponseEntity("수정완료", HttpStatus.OK);
    }

    //사무공간 상세페이지에서 세부공간 선택
    @Operation(summary = "세부 사무공간 선택", description = "세부 사무공간 선택을 선택하여 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/roomOne/{roomId}")
    public ResponseEntity findOneSpace(@PathVariable("roomId") final Long roomId) {
        return ResponseEntity.ok(roomService.getRoomDetailPage(roomId));
    }

    //세부공간 삭제
    @Operation(summary = "세부 사무공간 삭제", description = "백오피스페이지에서 세부 사무공간을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @DeleteMapping("/host/roomDelete/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }

    //세부공간 이미지 선택 삭제
    @Operation(summary = "세부 사무공간 이미지 단일삭제", description = "세부 사무공간 수정 시 사진을 선택삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @DeleteMapping("/roomImgDelete")
    public ResponseEntity<Room> roomImgDelete(@ModelAttribute RoomImgDeleteDto dto) {
        log.info("roomImgDelete : {}", dto);
        roomService.deleteRoomImg(dto);

        return new ResponseEntity("삭제완료", HttpStatus.OK);
    }

}
