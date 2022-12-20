package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Qna;
import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterInsertDto;
import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterSearchDto;
import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterUpdateDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.service.CustomerCenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "CustomerCenterController", description = "고객센터 일대일 문의 관련 api입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerCenterController {

    private final CustomerCenterService customerCenterService;

    //마스터페이지에서 일대일 문의 리스트 출력 - 유저 / 호스트 sorting, 문의타입 sorting
    @Operation(summary = "일대일 문의 전체 글 목록 조회 요청", description = "마스터관리페이지에서 일대일 문의 전체 글을 출력하고 회원등급, 문의종류별로 소팅합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ListResult.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/customerCenter")
    public ResponseEntity<ListResult> customerCenterMasterPage(@ModelAttribute CustomerCenterSearchDto dto) {
        log.info("CustomerCenterSearchDto :{}", dto);
        PageRequest pageRequest = PageRequest.of(dto.getPageNum(), 10);
        return ResponseEntity.ok(customerCenterService.getccMasterManage(pageRequest, dto));
    }

    //마이페이지에서 일대일 문의 리스트 출력
    @Operation(summary = "나의 일대일 문의 목록 조회 요청", description = "내가 작성한 일대일 문의 글 목록을 조회하고 문의종류별로 소팅합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ListResult.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/myCustomerCenter")
    public ResponseEntity<ListResult> myPageccList(@ModelAttribute CustomerCenterSearchDto dto) {
        log.info("CustomerCenterSearchDto :{}", dto);
        PageRequest pageRequest = PageRequest.of(dto.getPageNum(), 10);
        return ResponseEntity.ok(customerCenterService.getMyCustomerCenterList(pageRequest, dto));
    }

    //1대1문의 작성
    @Operation(summary = "일대일 문의 글 작성", description = "일대일 문의 글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/ccCreate")
    public ResponseEntity<Qna> writeCc(@Valid @RequestBody CustomerCenterInsertDto dto) {

        customerCenterService.createCustmerCenter(dto);

        return new ResponseEntity("입력완료", HttpStatus.OK);
    }

    //1대1문의 수정
    @Operation(summary = "일대일 문의 글 수정", description = "일대일 문의 글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/ccUpdate/{ccId}")
    public ResponseEntity<Qna> updateCc(@Valid @RequestBody CustomerCenterUpdateDto dto, @PathVariable Long ccId) {

        customerCenterService.updateCustomerCenter(ccId, dto);

        return new ResponseEntity("수정완료", HttpStatus.OK);
    }

    //1대1문의 삭제
    @Operation(summary = "일대일 문의 글 삭제", description = "일대일 문의 글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @DeleteMapping("/ccDelete/{ccId}")
    public ResponseEntity<Void> deleteCc(@PathVariable Long ccId) {
        customerCenterService.deleteCustomerCenter(ccId);
        return ResponseEntity.ok().build();
    }

}
