package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.MyIamport;
import com.golfzonTech4.worktalk.dto.pay.PayInsertDto;
import com.golfzonTech4.worktalk.dto.pay.PayWebhookDto;
import com.golfzonTech4.worktalk.service.PayService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.BillingCustomer;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.siot.IamportRestClient.response.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PayController {
    private final MyIamport myIamport;
    private final PayService payService;

    /**
     * 선결제 데이터 검증 및 저장 요청
     */
    @PostMapping("/payments/prepaid")
    public IamportResponse<Payment> prepaid(
            @RequestBody PayInsertDto dto)
            throws IamportResponseException, IOException {
        log.info("getResult : {}", dto);
        IamportResponse<Payment> response = myIamport.getClient().paymentByImpUid(dto.getImp_uid());
        BigDecimal serverAmount = response.getResponse().getAmount();
        BigDecimal clientAmount = BigDecimal.valueOf(dto.getPayAmount());
        log.info("clientAmount : {}, serverAmount : {}", clientAmount, serverAmount);
        if (!clientAmount.equals(serverAmount)) {
            new IllegalStateException("잘못된 가격값입니다.");
        }
        Long result = payService.save(dto).getPayId();
        log.info("result: {}", result);
        return response;
    }

    /**
     * 보증금 결제 등록 후 결제 예약 요청
     */
    @PostMapping("/payments/schedule")
    public IamportResponse<List<Schedule>> schedule(
            @RequestBody PayInsertDto dto)
            throws IamportResponseException, IOException {
        IamportClient client = myIamport.getClient();
        BillingCustomer bc = client.getBillingCustomer(dto.getCustomer_uid()).getResponse();
        String customerUid = bc.getCustomerUid();
        log.info("cu : {}", customerUid);

        return payService.schedule(dto);
    }

    /**
     * 후결제 내역 검증 및 저장
     */
    @PostMapping("/payments/postpaid")
    public void postpaid(
            @RequestBody PayWebhookDto dto) throws IamportResponseException, IOException {
        log.info("postpaid: dto");

        payService.postpaid(dto);
    }
}
