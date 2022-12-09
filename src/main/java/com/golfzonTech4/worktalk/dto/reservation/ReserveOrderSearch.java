package com.golfzonTech4.worktalk.dto.reservation;

import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.domain.ReserveStatus;
import com.golfzonTech4.worktalk.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveOrderSearch {

    private int pageNum;
    private Integer paid;
    private Integer spaceType;
    private ReserveStatus reserveStatus;
    private PaymentStatus paymentStatus;
    private RoomType roomType;

}
