package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Qna;
import com.golfzonTech4.worktalk.dto.qna.QnaInsertDto;
import com.golfzonTech4.worktalk.dto.qna.QnaUpdateDto;
import com.golfzonTech4.worktalk.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 작성자 : 최수연
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    //사무공간상세페이지에서 QnA 리스트 출력
    @GetMapping("/spaceOne/{spaceId}/qnas")
    public ResponseEntity qnaListBySpace(@PathVariable("spaceId") Long spaceId){
        return ResponseEntity.ok(qnaService.getQnasBySpace(spaceId));
    }

    //마이페이지에서 QnA 리스트 출력
    @GetMapping("/myqnas")
    public ResponseEntity myQnaListPage(){
        return ResponseEntity.ok(qnaService.getMyQnas());
    }

    @PostMapping("/qnaCreate")
    public ResponseEntity<Qna> createQna(@Valid @RequestBody QnaInsertDto dto){

        qnaService.createQna(dto);

        return new ResponseEntity("입력완료", HttpStatus.OK);
    }

    @PostMapping("/qnaUpdate/{qnaId}")
    public ResponseEntity<Qna> updateQna(@Valid @RequestBody QnaUpdateDto dto, @PathVariable Long qnaId){

        qnaService.updateQna(qnaId, dto);

        return new ResponseEntity("수정완료", HttpStatus.OK);
    }

    //QnA 삭제
    @DeleteMapping("/qnaDelete/{qnaId}")
    public ResponseEntity<Void> deleteQna(@PathVariable Long qnaId){
        qnaService.deleteQna(qnaId);
        return ResponseEntity.ok().build();
    }

}
