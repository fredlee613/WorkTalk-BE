package com.golfzonTech4.worktalk.dto.reservation;

import com.golfzonTech4.worktalk.domain.*;
import com.golfzonTech4.worktalk.dto.pay.PayDto;
import com.golfzonTech4.worktalk.dto.pay.PaySimpleDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Data
@NoArgsConstructor
public class ReserveSimpleDto {
    // 공간명(space), 세부공간명(room), 예약번호(reserve), 예약일(reserve),
    // 사용일(reserve), 예약자(reserve), 예약 상태(reserve), 공급자 연락처(space_member)

    private String roomName;
    private int paid;
    private Long reserveId;
    private Long memberId;
    private Long roomId;
    private BookDate bookDate;
    private String name;
    private ReserveStatus reserveStatus;
    private PaymentStatus paymentStatus;
    private RoomType roomType;
    private int reserveAmount;
    private List<PayDto> pays;
    private String cancelReason;
    private Long reviewId;

    @QueryProjection
    public ReserveSimpleDto(String roomName, int paid, Long reserveId, Long memberId, Long roomId, BookDate bookDate, String name,
                            ReserveStatus reserveStatus, PaymentStatus paymentStatus, RoomType roomType, int reserveAmount, String cancelReason) {
        this.roomName = roomName;
        this.paid = paid;
        this.reserveId = reserveId;
        this.memberId = memberId;
        this.roomId = roomId;
        this.bookDate = bookDate;
        this.name = name;
        this.reserveStatus = reserveStatus;
        this.paymentStatus = paymentStatus;
        this.roomType = roomType;
        this.reserveAmount = reserveAmount;
        this.cancelReason = cancelReason;
    }

    @QueryProjection
    public ReserveSimpleDto(String roomName, int paid, Long reserveId, Long memberId, Long roomId, BookDate bookDate,
                            String name, ReserveStatus reserveStatus, PaymentStatus paymentStatus, RoomType roomType,
                            int reserveAmount, String cancelReason, Long reviewId) {
        this.roomName = roomName;
        this.paid = paid;
        this.reserveId = reserveId;
        this.memberId = memberId;
        this.roomId = roomId;
        this.bookDate = bookDate;
        this.name = name;
        this.reserveStatus = reserveStatus;
        this.paymentStatus = paymentStatus;
        this.roomType = roomType;
        this.reserveAmount = reserveAmount;
        this.cancelReason = cancelReason;
        this.reviewId = reviewId;
    }

    @QueryProjection
    public ReserveSimpleDto(String roomName, int paid, Long reserveId, Long memberId, Long roomId, BookDate bookDate, String name, ReserveStatus reserveStatus, PaymentStatus paymentStatus, RoomType roomType, int reserveAmount, List<PayDto> pays) {
        this.roomName = roomName;
        this.paid = paid;
        this.reserveId = reserveId;
        this.memberId = memberId;
        this.roomId = roomId;
        this.bookDate = bookDate;
        this.name = name;
        this.reserveStatus = reserveStatus;
        this.paymentStatus = paymentStatus;
        this.roomType = roomType;
        this.reserveAmount = reserveAmount;
        this.pays = pays;
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

    @QueryProjection
    public ReserveSimpleDto(String roomName, BookDate bookDate) {
        this.roomName = roomName;
        this.bookDate = bookDate;
    }
}
