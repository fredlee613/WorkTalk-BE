package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @Column(name = "CREATE_DATE", updatable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(name = "LAST_MODIFIED_DATE")
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

//    @PrePersist
//    public void onPrePersist(){
//        this.createDate = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        this.lastModifiedDate = this.createDate;
//    }
//
//    @PreUpdate
//    public void onPreUpdate(){
//        this.lastModifiedDate = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//    }
    //TIMESTAMP형식을 VARCHAR로 바꿔야 사용가능..
}
