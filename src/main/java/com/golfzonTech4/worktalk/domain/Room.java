package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@Getter
@SequenceGenerator(name = "SEQ_ROOM_GENERATOR", sequenceName = "SEQ_ROOM", initialValue = 1, allocationSize = 50)
public class Room implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ROOM_GENERATOR")
    @Column(name = "ROOM_ID")
    private Long room_id;

    @ManyToOne
    @JoinColumn(name = "SPACE_ID", nullable = false)
    private Space space;

    @Enumerated(EnumType.STRING)
    private Roomtype room_type;

    @Column(name = "ROOM_NAME", nullable = false, length = 50)
    private String room_name;

    @Column(name = "ROOM_DETAIL", nullable = true, length = 500)
    private String room_detail;

    @Column(name = "ROOM_IMG", nullable = true, length = 100)
    private String room_img;

    @Column(name = "ROOM_PRICE", nullable = false)
    private int room_price;

    @Column(name = "WORK_START", nullable = false)
    private int work_start;

    @Column(name = "WORK_END", nullable = false)
    private int work_end;

}
