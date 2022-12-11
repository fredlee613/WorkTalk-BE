package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.domain.MyIamport;
import com.golfzonTech4.worktalk.dto.pay.PayInsertDto;
import com.golfzonTech4.worktalk.dto.pay.PayOrderSearch;
import com.golfzonTech4.worktalk.dto.pay.PaySimpleDto;
import com.golfzonTech4.worktalk.dto.pay.PayWebhookDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.repository.pay.PayRepository;
import com.golfzonTech4.worktalk.service.PayService;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PayController {
    private final PayService payService;
    private final PayRepository payRepository;
    private final MyIamport myIamport;

    /**
     * 선결제 데이터 검증 및 저장 요청
     */
    @PostMapping("/payments/prepaid")
    public ResponseEntity<Long> prepaid( @RequestBody PayInsertDto dto) throws IamportResponseException, IOException {
        log.info("getResult : {}", dto);
        Long result = payService.prepaid(dto);
        return ResponseEntity.ok(result);
    }

    /**
     * 후결제 내역 검증 및 저장
     */
    @PostMapping("/payments/postpaid")
    public void postpaid(
            @RequestBody PayWebhookDto dto) throws IamportResponseException, IOException {
        log.info("postpaid: dto");
        if(dto.getMerchant_uid().contains("예약결제")){
            log.info("heading to postpaid....");
            payService.postpaid(dto);}

    }

    /**
     * 결제건이 있는 방들의 이름 조회 요청
     */
    @GetMapping("/payments/rooms")
    public ResponseEntity<Set<String>> findRooms() {
        log.info("findRooms");
        return ResponseEntity.ok(payService.findRooms());
    }

    /**
     * 결제 이력 조회 요청
     */
    @GetMapping("/payments/history")
    public ResponseEntity<ListResult> findByName(@ModelAttribute PayOrderSearch dto) {
        log.info("findByUserPage : {}", dto);
        PageRequest pageRequest = PageRequest.of(dto.getPageNum(), 10);
        return ResponseEntity.ok(payService.findByName(dto, pageRequest));
    }
}
