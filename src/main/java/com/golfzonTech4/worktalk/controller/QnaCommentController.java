package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Qna;
import com.golfzonTech4.worktalk.dto.qnacomment.QnaCommentInsertDto;
import com.golfzonTech4.worktalk.dto.qnacomment.QnaCommentUpdateDto;
import com.golfzonTech4.worktalk.service.QnaCommentService;
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

@Tag(name = "QnACommentController", description = "사무공간 Q&A 답댓글 관련 api입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
public class QnaCommentController {

    private final QnaCommentService qnaCommentService;

    //답글 등록
    @Operation(summary = "Q&A 답변 댓글 작성", description = "Q&A 답변 댓글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/qnacommentCreate")
    public ResponseEntity<Qna> createQnaComment(@Valid @RequestBody QnaCommentInsertDto dto) {

        qnaCommentService.createQnaComment(dto);

        return new ResponseEntity("입력완료", HttpStatus.OK);
    }

    //답글 수정
    @Operation(summary = "Q&A 답변 댓글 수정", description = "Q&A 답변 댓글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/qnacommentUpdate/{qnaCommentId}")
    public ResponseEntity<Qna> updateQnaComment(@Valid @RequestBody QnaCommentUpdateDto dto, @PathVariable Long qnaCommentId) {

        qnaCommentService.updateQnaComment(qnaCommentId, dto);

        return new ResponseEntity("수정완료", HttpStatus.OK);
    }

    //답글 삭제
    @Operation(summary = "Q&A 답변 댓글 삭제", description = "Q&A 답변 댓글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @DeleteMapping("/qnacommentDelete/{qnaCommentId}")
    public ResponseEntity<Void> deleteQnaComment(@PathVariable Long qnaCommentId) {
        qnaCommentService.deleteQnaComment(qnaCommentId);
        return ResponseEntity.ok().build();
    }

}
