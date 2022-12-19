package com.golfzonTech4.worktalk.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class HostDto {

    private Long id;
    private String name;

    private int activated;

    @QueryProjection
    public HostDto(Long id, String name, int activated) {
        this.id = id;
        this.name = name;
        this.activated = activated;
    }
}
