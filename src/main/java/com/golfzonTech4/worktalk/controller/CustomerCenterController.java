package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Qna;
import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterInsertDto;
import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterUpdateDto;
import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterSearchDto;
import com.golfzonTech4.worktalk.service.CustomerCenterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerCenterController {

    private final CustomerCenterService customerCenterService;

    //마스터페이지에서 1대1문의 리스트 출력 - 유저 / 호스트 sorting, 문의타입 sorting
    @GetMapping("/customerCenter")
    public ResponseEntity customerCenterMasterPage(@ModelAttribute CustomerCenterSearchDto dto){
        return ResponseEntity.ok(customerCenterService.getccMasterManage(dto));
    }

    //마스터페이지에서 1대1문의 문의타입 sorting
//    @GetMapping("/customerCenter/type")
//    public ResponseEntity customerCenterMasterccType(@ModelAttribute CustomerCenterSearchDto dto){
//        return ResponseEntity.ok(customerCenterService.getccMasterManage(dto));
//    }

    //마이페이지에서 1대1문의 리스트 출력
    @GetMapping("/myCustomerCenter")
    public ResponseEntity myPageccList(){
        return ResponseEntity.ok(customerCenterService.getMyCustomerCenterList());
    }

    //1대1문의 작성
    @PostMapping("/ccCreate")
    public ResponseEntity<Qna> writeCc(@Valid @RequestBody CustomerCenterInsertDto dto){

        customerCenterService.createCustmerCenter(dto);

        return new ResponseEntity("입력완료", HttpStatus.OK);
    }

    //1대1문의 수정
    @PostMapping("/ccUpdate/{ccId}")
    public ResponseEntity<Qna> updateCc(@Valid @RequestBody CustomerCenterUpdateDto dto, @PathVariable Long ccId){

        customerCenterService.updateCustomerCenter(ccId, dto);

        return new ResponseEntity("수정완료", HttpStatus.OK);
    }

    //1대1문의 삭제
    @DeleteMapping("/ccDelete/{ccId}")
    public ResponseEntity<Void> deleteCc(@PathVariable Long ccId){
        customerCenterService.deleteCustomerCenter(ccId);
        return ResponseEntity.ok().build();
    }

}
