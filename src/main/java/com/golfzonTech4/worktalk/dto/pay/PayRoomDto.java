package com.golfzonTech4.worktalk.dto.pay;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class PayRoomDto {

    private String roomName;
    private Integer spaceType;

    @QueryProjection
    public PayRoomDto(String roomName, Integer spaceType) {
        this.roomName = roomName;
        this.spaceType = spaceType;
    }
}
