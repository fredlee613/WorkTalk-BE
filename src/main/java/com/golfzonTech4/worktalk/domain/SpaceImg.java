package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "SPACE_IMG")
@SequenceGenerator(name = "SEQ_SPACE_IMG_GENERATOR", sequenceName = "SEQ_SPACE_IMG", initialValue = 1, allocationSize = 50)
public class SpaceImg {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SPACE_IMG_GENERATOR")
    @Column(name = "SPACE_IMG_ID")
    private Long spaceImgId;

    @Column(name = "IMG_NAME")
    private String imgName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPACE_ID")
    private Space space;

    public SpaceImg(String imgName, Space space) {
        this.imgName = imgName;
        this.space = space;
    }

    public void updateSpaceImg(String imgName){
        this.imgName = imgName;
    }

}
