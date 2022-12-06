package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.CustomerComment;
import com.golfzonTech4.worktalk.domain.Qna;
import com.golfzonTech4.worktalk.dto.customercomment.CustomerCommentInsertDto;
import com.golfzonTech4.worktalk.dto.customercomment.CustomerCommentUpdateDto;
import com.golfzonTech4.worktalk.service.CustomerCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerCommentController {

    private final CustomerCommentService customerCommentService;

    //답글 등록
    @PostMapping("/customercommentCreate")
    public ResponseEntity<CustomerComment> createCustomerComment(@Valid @RequestBody CustomerCommentInsertDto dto){

        customerCommentService.createCustomerComment(dto);

        return new ResponseEntity("입력완료", HttpStatus.OK);
    }

    //답글 수정
    @PostMapping("/customercommentUpdate/{ccId}")
    public ResponseEntity<Qna> updateCustomerComment(@Valid @RequestBody CustomerCommentUpdateDto dto, @PathVariable Long ccId){

        customerCommentService.updateCustomerComment(ccId, dto);

        return new ResponseEntity("수정완료", HttpStatus.OK);
    }

    //답글 삭제
    @DeleteMapping("/customercommentDelete/{ccId}")
    public ResponseEntity<Void> deleteCustomerComment(@PathVariable Long ccId){
        customerCommentService.deleteCustomerComment(ccId);
        return ResponseEntity.ok().build();
    }

}
