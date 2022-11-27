package com.golfzonTech4.worktalk.dto.reservation;

import com.golfzonTech4.worktalk.domain.BookDate;
import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.domain.ReserveStatus;
import com.golfzonTech4.worktalk.domain.RoomType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReserveSimpleDto {
    // 공간명(space), 세부공간명(room), 예약번호(reserve), 예약일(reserve),
    // 사용일(reserve), 예약자(reserve), 예약 상태(reserve), 공급자 연락처(space_member)

    private String roomName;
    private int paid;
    private Long reserveId;
    private Long memberId;
    private Long rooomId;
    private BookDate bookDate;
    private String name;
    private ReserveStatus reserveStatus;
    private PaymentStatus paymentStatus;
    private RoomType roomType;



    @QueryProjection
    public ReserveSimpleDto(String roomName, Long reserveId, Long memberId, Long rooomId, BookDate bookDate, String name, ReserveStatus reserveStatus, PaymentStatus paymentStatus, RoomType roomType, int paid) {
        this.roomName = roomName;
        this.reserveId = reserveId;
        this.memberId = memberId;
        this.rooomId = rooomId;
        this.bookDate = bookDate;
        this.name = name;
        this.reserveStatus = reserveStatus;
        this.paymentStatus = paymentStatus;
        this.roomType = roomType;
        this.paid = paid;
    }

    @QueryProjection
    public ReserveSimpleDto(Long reserveId, BookDate bookDate, String name, ReserveStatus reserveStatus) {
        this.reserveId = reserveId;
        this.bookDate = bookDate;
        this.name = name;
        this.reserveStatus = reserveStatus;
    }
    @QueryProjection
    public ReserveSimpleDto(Long reserveId, Long memberId) {
        this.reserveId = reserveId;
        this.memberId = memberId;
    }
}
