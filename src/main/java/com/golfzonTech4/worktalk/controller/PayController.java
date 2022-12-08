package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.dto.pay.PayInsertDto;
import com.golfzonTech4.worktalk.dto.pay.PayOrderSearch;
import com.golfzonTech4.worktalk.dto.pay.PayWebhookDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.service.PayService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PayController {
    private final PayService payService;

    /**
     * 선결제 데이터 검증 및 저장 요청
     */
    @PostMapping("/payments/prepaid")
    public ResponseEntity<Long> prepaid( @RequestBody PayInsertDto dto) throws IamportResponseException, IOException {
        log.info("getResult : {}", dto);
        Long result = payService.save(dto).getPayId();
        return ResponseEntity.ok(result);
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

    /**
     * 결제 이력 조회 요청
     */
    @GetMapping("/payments/history/{pageNum}")
    public ResponseEntity<ListResult> findByName(@PathVariable("pageNum") int pageNum, @RequestBody PayOrderSearch orderSearch) {
        log.info("findByUserPage : {} , {}", pageNum, orderSearch);
        PageRequest pageRequest = PageRequest.of(pageNum, 10);
        return ResponseEntity.ok(payService.findByName(orderSearch, pageRequest));
    }
}
