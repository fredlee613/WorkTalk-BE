package com.golfzonTech4.worktalk.dto.reservation;

import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveOrderSearch {

    private Integer paid;
    private PaymentStatus paymentStatus;
    private RoomType roomType;

}
