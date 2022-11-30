package com.golfzonTech4.worktalk.dto.pay;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
public class PayScheduleDto {

    private Long reserveId;
    private String customer_uid;
    private String merchant_uid;
    private LocalDateTime payDate;
    private int amount;

}
