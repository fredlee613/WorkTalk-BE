package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.dto.pay.PayInsertDto;
import com.golfzonTech4.worktalk.dto.pay.PayOrderSearch;
import com.golfzonTech4.worktalk.dto.pay.PayWebhookDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.service.PayService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

@Tag(name = "PayController", description = "결제 관련 api입니다.")
@RestController
@Slf4j
@RequiredArgsConstructor
public class PayController {
    private final PayService payService;

    /**
     * 선결제 데이터 검증 및 저장 요청
     */
    @Operation(summary = "선결제 데이터 검증 및 저장 요청", description = "해당 결제 건에 대한 데이터 검증 후 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/payments/prepaid")
    public ResponseEntity<Long> prepaid( @RequestBody PayInsertDto dto) throws IamportResponseException, IOException {
        log.info("getResult : {}", dto);
        Long result = payService.prepaid(dto);
        return ResponseEntity.ok(result);
    }

    /**
     * 후결제 내역 검증 및 저장
     */
    @Operation(summary = "후결제 내역 검증 및 저장", description = "웹훅으로 전달된 데이터를 검증 후 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
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
    @Operation(summary = "결제건이 있는 방들의 이름 조회 요청", description = "해당 호스트의 예약 건들이 존재하는 방들의 이름을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Set.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/payments/rooms")
    public ResponseEntity<Set<String>> findRooms() {
        log.info("findRooms");
        return ResponseEntity.ok(payService.findRooms());
    }

    /**
     * 결제 이력 조회 요청
     */
    @Operation(summary = "결제 이력 조회 요청", description = "해당 유저/ 호스트의 예약 건들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ListResult.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/payments/history")
    public ResponseEntity<ListResult> findByName(@ModelAttribute PayOrderSearch dto) {
        log.info("findByUserPage : {}", dto);
        PageRequest pageRequest = PageRequest.of(dto.getPageNum(), 10);
        return ResponseEntity.ok(payService.findByName(dto, pageRequest));
    }
}
