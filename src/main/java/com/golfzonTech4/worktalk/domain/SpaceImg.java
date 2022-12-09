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

    @Column(name = "SPACE_IMG_URL", length = 200)
    private String spaceImgUrl;

    @Column(name = "REP_IMG_YN", length = 20)
    private String repImgYn; //대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPACE_ID", nullable = false)
    private Space space;

    public SpaceImg(String spaceImgUrl, Space space) {
        this.spaceImgUrl = spaceImgUrl;
        this.space = space;
    }

    public void updateSpaceImg(String spaceImgUrl){
        this.spaceImgUrl = spaceImgUrl;
    }

}
