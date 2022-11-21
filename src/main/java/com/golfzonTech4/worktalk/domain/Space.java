package com.golfzonTech4.worktalk.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter @Setter
@SequenceGenerator(name = "SEQ_SPACE_GENERATOR", sequenceName = "SEQ_SPACE", initialValue = 1, allocationSize = 50)
public class Space implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SPACE_GENERATOR")
    @Column(name = "SPACE_ID")
    private Long space_id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "SPACE_NAME", nullable = false, length = 50)
    private String space_name;

    @Column(name = "SPACE_DETAIL", nullable = true, length = 1000)
    private String space_detail;

    @Column(name = "POSTCODE", nullable = false, length = 20)
    private String postcode;

    @Column(name = "ADDRESS", nullable = false, length = 100)
    private String address;

    @Column(name = "DETAIL_ADDRESS", nullable = false, length = 100)
    private String detail_address;

    @Column(name = "REG_CODE", nullable = false, length = 50)
    private String reg_code;

    @Column(name = "SPACE_TYPE", nullable = false)
    private int space_type;

    @Column(name = "SPACE_STATUS", nullable = false, length = 20)
    private String space_status;

    @Column(name = "SPACE_IMG", nullable = true, length = 100)
    private String space_img;
}
