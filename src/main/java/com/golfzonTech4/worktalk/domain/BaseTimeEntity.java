package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseTimeEntity {

    @ColumnDefault(value="sysdate")
    @Column(name = "CREATE_DATE", insertable = true, updatable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @ColumnDefault(value="sysdate")
    @Column(name = "MODIFIED_DATE", insertable = false, updatable = true)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
