package com.golfzonTech4.worktalk.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListResult<T> {

    private Long count;
    private Long sum;
    private T data;

    public ListResult(Long count, T data) {
        this.count = count;
        this.data = data;
    }
}
