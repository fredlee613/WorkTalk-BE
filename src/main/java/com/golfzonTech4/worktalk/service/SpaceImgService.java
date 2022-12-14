package com.golfzonTech4.worktalk.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.golfzonTech4.worktalk.domain.SpaceImg;
import com.golfzonTech4.worktalk.dto.space.SpaceImgDto;
import com.golfzonTech4.worktalk.repository.space.SpaceImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SpaceImgService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final SpaceImgRepository spaceImgRepository;

    // 이미지 삭제
    public void deleteSpaceImg(SpaceImgDto dto){
        Optional<SpaceImg> findSpaceImg = spaceImgRepository.findById(dto.getSpaceImgId());
        String spaceImgUrl = findSpaceImg.get().getSpaceImgUrl();
        try {
            log.info("deleteSpaceImg : ", spaceImgUrl.substring(spaceImgUrl.lastIndexOf("/")+1));
            amazonS3.deleteObject(this.bucket, spaceImgUrl.substring(spaceImgUrl.lastIndexOf("/")+1)); // s3에서 이미지 삭제
        } catch (AmazonServiceException e){
            log.error(e.getErrorMessage());
        }
        spaceImgRepository.deleteById(dto.getSpaceImgId()); // DB에서 이미지 삭제
    }

}
