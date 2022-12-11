package com.golfzonTech4.worktalk.dto.room;

import com.golfzonTech4.worktalk.domain.RoomType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RoomDetailDto {

    private Long roomId;

    private String roomName;
    private String roomDetail;

    private RoomType roomType;

    private int roomPrice;

    private int workStart;

    private int workEnd;

    private List<RoomImgDto> roomImgDtoList = new ArrayList<>();

    @QueryProjection
    public RoomDetailDto(Long roomId, String roomName, String roomDetail,
                         RoomType roomType, int roomPrice, int workStart, int workEnd) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomDetail = roomDetail;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.workStart = workStart;
        this.workEnd = workEnd;
    }
}
