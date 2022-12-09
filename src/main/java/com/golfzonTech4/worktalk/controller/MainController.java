package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.dto.space.SpaceSearchDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.service.SpaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {
    private final SpaceService spaceService;

    //메인페이지 - 전체 사무공간 목록, (오피스,데스크,회의실) 필터기능, 사무공간명, 주소 키워드 검색
    @GetMapping(value = "/main")
    public ResponseEntity<ListResult> mainPage(@ModelAttribute SpaceSearchDto dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPageNum(), 9);
        log.info("dto :{}", dto);
        return ResponseEntity.ok(spaceService.getMainSpacePage(pageRequest, dto));
    }
}
