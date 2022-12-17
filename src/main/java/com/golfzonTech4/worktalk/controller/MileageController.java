package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.service.MileageService;
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
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "MileageController", description = "마일리지 관련 api입니다.")
@Controller
@Slf4j
@RequiredArgsConstructor
public class MileageController {

    public final MileageService mileageService;

    @Operation(summary = "마일리지 사용 내역 조회 요청", description = "해당 사용자의 마일리지 사용 이력을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ListResult.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/mileage")
    public ResponseEntity<ListResult> findAll() {
        log.info("findAll");
        ListResult result = mileageService.findAllByName();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "마일리지 사용 가능액 조회 요청", description = "해당 사용자의 마일리지 사용 가능 금액을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/mileage/total")
    public ResponseEntity<Integer> getTotal() {
        log.info("getTotal");
        int total = mileageService.getTotal();
        return ResponseEntity.ok(total);
    }
}
