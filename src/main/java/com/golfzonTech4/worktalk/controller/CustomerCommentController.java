package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.CustomerComment;
import com.golfzonTech4.worktalk.domain.Qna;
import com.golfzonTech4.worktalk.dto.customercomment.CustomerCommentInsertDto;
import com.golfzonTech4.worktalk.dto.customercomment.CustomerCommentUpdateDto;
import com.golfzonTech4.worktalk.service.CustomerCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "CustomerCommentController", description = "고객센터 일대일 문의 답변글 관련 api입니다.")

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerCommentController {

    private final CustomerCommentService customerCommentService;

    //답글 등록
    @Operation(summary = "일대일 문의 답변 작성", description = "일대일 문의 답변을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/customercommentCreate")
    public ResponseEntity<CustomerComment> createCustomerComment(@Valid @RequestBody CustomerCommentInsertDto dto) {

        customerCommentService.createCustomerComment(dto);

        return new ResponseEntity("입력완료", HttpStatus.OK);
    }

    //답글 수정
    @Operation(summary = "일대일 문의 답변 수정", description = "일대일 문의 답변을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/customercommentUpdate/{ccCommentId}")
    public ResponseEntity<Qna> updateCustomerComment(@Valid @RequestBody CustomerCommentUpdateDto dto, @PathVariable Long ccCommentId) {

        customerCommentService.updateCustomerComment(ccCommentId, dto);

        return new ResponseEntity("수정완료", HttpStatus.OK);
    }

    //답글 삭제
    @Operation(summary = "일대일 문의 답변 삭제", description = "일대일 문의 답변을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @DeleteMapping("/customercommentDelete/{ccCommentId}")
    public ResponseEntity<Void> deleteCustomerComment(@PathVariable Long ccCommentId) {
        customerCommentService.deleteCustomerComment(ccCommentId);
        return ResponseEntity.ok().build();
    }

}
