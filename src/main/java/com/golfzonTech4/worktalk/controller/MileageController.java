package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.service.MileageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MileageController {

    public final MileageService mileageService;

    @GetMapping("mileage/")
    public ResponseEntity<ListResult> findAll() {
        log.info("findAll");
        ListResult result = mileageService.findAllByName();
        return ResponseEntity.ok(result);
    }

    @GetMapping("mileage/total")
    public ResponseEntity<Integer> getTotal() {
        log.info("getTotal");
        int total = mileageService.getTotal();
        return ResponseEntity.ok(total);
    }
}
