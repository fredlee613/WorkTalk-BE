package com.golfzonTech4.worktalk.dto.pay;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class PayWebhookDto {

    private String imp_uid;
    private String merchant_uid;
    private String status;
}
