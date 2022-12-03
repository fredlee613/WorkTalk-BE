package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.dto.mileage.MileageFindDto;
import com.golfzonTech4.worktalk.service.MileageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MileageController {

    public final MileageService mileageService;

    @GetMapping("mileage/")
    public ResponseEntity<List<MileageFindDto>> findAll() {
        log.info("findAll : {}");
        List<MileageFindDto> result = mileageService.findAllByName();
        log.info("result.size() : {}", result.size());
        return ResponseEntity.ok(result);
    }
}
