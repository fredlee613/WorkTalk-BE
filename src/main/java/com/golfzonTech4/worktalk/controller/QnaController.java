package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Qna;
import com.golfzonTech4.worktalk.dto.qna.QnaInsertDto;
import com.golfzonTech4.worktalk.dto.qna.QnaSearchDto;
import com.golfzonTech4.worktalk.dto.qna.QnaSpacePagingDto;
import com.golfzonTech4.worktalk.dto.qna.QnaUpdateDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.service.QnaService;
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

@Tag(name = "QnAController", description = "사무공간 Q&A 관련 api입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    //사무공간상세페이지에서 QnA 리스트 출력
    @Operation(summary = "해당 사무공간 Q&A 글 목록 조회 요청", description = "사무공간상세페이지에서 Q&A 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ListResult.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/spaceOne/qnas")
    public ResponseEntity<ListResult> qnaListBySpace(@ModelAttribute QnaSpacePagingDto dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPageNum(), 5);
        return ResponseEntity.ok(qnaService.getQnasBySpace(pageRequest, dto.getSpaceId()));
    }

    //마이페이지에서 QnA 리스트 출력
    @Operation(summary = "나의 Q&A 글 목록 조회 요청",
            description = "나의 Q&A관리페이지에서 내가 작성한 Q&A 글 목록을 조회하고 문의종류별로 소팅합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ListResult.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/myqnas")
    public ResponseEntity<ListResult> myQnaListPage(@ModelAttribute QnaSearchDto dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPageNum(), 10);
        return ResponseEntity.ok(qnaService.getMyQnas(pageRequest, dto));
    }

    //호스트의 모든 사무공간 QnA관리 리스트 출력
    @Operation(summary = "호스트의 모든 사무공간 Q&A 글 목록 조회 요청",
            description = "호스트의 Q&A관리페이지에서 호스트가 소유한 모든 공간의 Q&A 목록을 조회하고 Q&A타입과 사무공간이름별로 소팅합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ListResult.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/manageHostQna")
    public ResponseEntity<ListResult> qnaListByHostSpace(@ModelAttribute QnaSearchDto dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPageNum(), 10);
        return ResponseEntity.ok(qnaService.getQnaHostManagePage(pageRequest, dto));
    }

    @Operation(summary = "Q&A 글 작성", description = "Q&A 문의 글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/qnaCreate")
    public ResponseEntity<Qna> createQna(@Valid @RequestBody QnaInsertDto dto) {
        qnaService.createQna(dto);
        return new ResponseEntity("입력완료", HttpStatus.OK);
    }

    @Operation(summary = "Q&A 글 수정", description = "Q&A 문의 글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/qnaUpdate/{qnaId}")
    public ResponseEntity<Qna> updateQna(@Valid @RequestBody QnaUpdateDto dto, @PathVariable Long qnaId) {
        qnaService.updateQna(qnaId, dto);
        return new ResponseEntity("수정완료", HttpStatus.OK);
    }

    @Operation(summary = "Q&A 글 삭제", description = "Q&A 문의 글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @DeleteMapping("/qnaDelete/{qnaId}")
    public ResponseEntity<Void> deleteQna(@PathVariable Long qnaId) {
        qnaService.deleteQna(qnaId);
        return ResponseEntity.ok().build();
    }

}
