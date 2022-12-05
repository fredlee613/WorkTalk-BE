package com.golfzonTech4.worktalk.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListResult<T> {

    private Long count;
    private T data;

}
