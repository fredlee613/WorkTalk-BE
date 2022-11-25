package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "SPACE_IMGG")
@SequenceGenerator(name = "SEQ_SPACE_IMG_GENERATOR", sequenceName = "SEQ_SPACE_IMG", initialValue = 1, allocationSize = 50)
public class SpaceImgg {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SPACE_IMG_GENERATOR")
    @Column(name = "SPACE_IMG_ID")
    private Long spaceImgId;

    @Column(name = "IMG_NAME")
    private String imgName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPACE_ID")
    private Space space;

    @JoinColumn(name = "IMG_URL")
    private String imgUrl;

    public void updateSpaceImg(String imgName, String imgUrl){
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
