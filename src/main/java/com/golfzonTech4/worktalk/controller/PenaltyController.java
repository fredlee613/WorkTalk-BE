package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.dto.penalty.PenaltyDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.service.PenaltyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PenaltyController", description = "페널티 관련 api입니다.")
@Slf4j
@Controller
@RequiredArgsConstructor
public class PenaltyController {
    private final PenaltyService penaltyService;

    /**
     * 페널티 부여 요청
     */
    @Operation(summary = "페널티 부여 요청", description = "해당 사용자에게 페널티를 부여하고 비활성화합니다. 비활성화 된 회원은 예약을 진행할 수 없습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/penalty/add")
    public ResponseEntity<Long> addPenalty(@RequestBody PenaltyDto dto) {
        log.info("addPenalty : {}", dto);
        Long result = penaltyService.addPenalty(dto);
        return ResponseEntity.ok(result);
    }

    /**
     * 페널티 회수 요청
     */
    @Operation(summary = "페널티 부여 요청", description = "해당 사용자에게 부여된 페널티를 삭제한 후 계정을 활성화합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/penalty/remove/{penaltyId}")
    public ResponseEntity removePenalty(@PathVariable(name = "penaltyId") Long penaltyId) {
        log.info("removePenalty : {}", penaltyId);
        penaltyService.removePenalty(penaltyId);
        return ResponseEntity.ok().build();
    }

    /**
     * 페널티 리스트 요청
     */
    @Operation(summary = "회원 조회 및 페널티 여부 조회", description = "전체 사용자 목록을 조회 후 페널티 여부를 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ListResult.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/penalty")
    public ResponseEntity<ListResult> findPenalties(@RequestParam(required = false) Integer activated) {
        log.info("findPenalties : {}", activated);
        ListResult result = penaltyService.findPenalties(activated);
        return ResponseEntity.ok(result);
    }
}
