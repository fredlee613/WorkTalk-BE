package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Qna;
import com.golfzonTech4.worktalk.dto.review.ReviewInsertDto;
import com.golfzonTech4.worktalk.dto.review.ReviewUpdateDto;
import com.golfzonTech4.worktalk.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //후기 등록
    @PostMapping("/reviewCreate")
    public ResponseEntity<Qna> createReview(@Valid @RequestBody ReviewInsertDto dto){

        reviewService.createReview(dto);

        return new ResponseEntity("입력완료", HttpStatus.OK);
    }

    //후기 수정
    @PostMapping("/reviewUpdate/{reviewId}")
    public ResponseEntity<Qna> updateReview(@Valid @RequestBody ReviewUpdateDto dto, @PathVariable Long reviewId){

        reviewService.updateReview(reviewId, dto);

        return new ResponseEntity("수정완료", HttpStatus.OK);
    }

    //후기 삭제
    @DeleteMapping("/reviewDelete/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

    //사무공간상세페이지에서 후기 리스트 출력
    @GetMapping("/spaceOne/{spaceId}/reviews")
    public ResponseEntity reviewListBySpace(@PathVariable("spaceId") Long spaceId){
        return ResponseEntity.ok(reviewService.getReviewsBySpace(spaceId));
    }

    //마이페이지에서 후기 리스트 출력
    @GetMapping("/myreviews")
    public ResponseEntity myReviewPage(){
        return ResponseEntity.ok(reviewService.getMyReviews());
    }
}
