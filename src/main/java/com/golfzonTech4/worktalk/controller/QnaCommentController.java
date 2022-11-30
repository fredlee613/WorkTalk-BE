package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Qna;
import com.golfzonTech4.worktalk.domain.QnaComment;
import com.golfzonTech4.worktalk.dto.qna.QnaInsertDto;
import com.golfzonTech4.worktalk.dto.qna.QnaUpdateDto;
import com.golfzonTech4.worktalk.dto.qnacomment.QnaCommentInsertDto;
import com.golfzonTech4.worktalk.dto.qnacomment.QnaCommentUpdateDto;
import com.golfzonTech4.worktalk.service.QnaCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QnaCommentController {

    private final QnaCommentService qnaCommentService;

    //답글 등록
    @PostMapping("/qnacommentCreate")
    public ResponseEntity<Qna> createQnaComment(@Valid @RequestBody QnaCommentInsertDto dto){

        qnaCommentService.createQnaComment(dto);

        return new ResponseEntity("입력완료", HttpStatus.OK);
    }

    //답글 수정
    @PostMapping("/qnacommentUpdate/{qnaId}")
    public ResponseEntity<Qna> updateQnaComment(@Valid @RequestBody QnaCommentUpdateDto dto, @PathVariable Long qnaId){

        qnaCommentService.updateQnaComment(qnaId, dto);

        return new ResponseEntity("수정완료", HttpStatus.OK);
    }

    ////답글 삭제
    @DeleteMapping("/qnacommentDelete/{qnaId}")
    public ResponseEntity<Void> deleteQnaComment(@PathVariable Long qnaId){
        qnaCommentService.deleteQnaComment(qnaId);
        return ResponseEntity.ok().build();
    }

}
