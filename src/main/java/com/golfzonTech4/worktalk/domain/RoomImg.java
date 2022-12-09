package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "ROOM_IMG")
@SequenceGenerator(name = "SEQ_ROOM_IMG_GENERATOR", sequenceName = "SEQ_ROOM_IMG", initialValue = 1, allocationSize = 50)
public class RoomImg {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SPACE_IMG_GENERATOR")
    @Column(name = "ROOM_IMG_ID")
    private Long roomImgId;

    @Column(name = "ROOM_IMG_URL", length = 200)
    private String roomImgUrl;

    @Column(name = "REP_IMG_YN", length = 20)
    private String repImgYn; //대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID", nullable = false)
    private Room room;

    public RoomImg(String roomImgUrl, Room room) {
        this.roomImgUrl = roomImgUrl;
        this.room = room;
    }

    public void updateSpaceImg(String roomImgUrl){
        this.roomImgUrl = roomImgUrl;
    }

}
