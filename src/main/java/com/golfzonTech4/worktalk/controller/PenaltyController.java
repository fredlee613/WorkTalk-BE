package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.dto.member.MemberSerachDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.dto.penalty.PenaltyDto;
import com.golfzonTech4.worktalk.service.PenaltyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PenaltyController {
    private final PenaltyService penaltyService;

    /**
     * 페널티 부여 요청
     */
    @PostMapping("/penalty/add")
    public ResponseEntity<Long> addPenalty(@RequestBody PenaltyDto dto) {
        log.info("addPenalty : {}", dto);
        Long result = penaltyService.addPenalty(dto);
        return ResponseEntity.ok(result);
    }

    /**
     * 페널티 회수 요청
     */
    @GetMapping("/penalty/remove/{penaltyId}")
    public ResponseEntity removePenalty(@PathVariable(name = "penaltyId") Long penaltyId) {
        log.info("removePenalty : {}", penaltyId);
        penaltyService.removePenalty(penaltyId);
        return ResponseEntity.ok().build();
    }

    /**
     * 페널티 리스트 요청
     */
    @GetMapping("/penalty")
    public ResponseEntity<ListResult> findPenalties(@ModelAttribute MemberSerachDto dto) {
        log.info("findPenalties");
        return ResponseEntity.ok(penaltyService.findPenalties(dto));
    }
}
