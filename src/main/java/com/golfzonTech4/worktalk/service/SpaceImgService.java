package com.golfzonTech4.worktalk.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.domain.SpaceImg;
import com.golfzonTech4.worktalk.repository.space.SpaceImgRepository;
import com.golfzonTech4.worktalk.repository.space.SpaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SpaceImgService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    private final SpaceRepository spaceRepository;
    private final SpaceImgRepository spaceImgRepository;

    public void deleteSpaceImg(Long spaceImgId){
//        Space findSpace = spaceRepository.findBySpaceId(spaceId);
        Optional<SpaceImg> findSpaceImg = spaceImgRepository.findById(spaceImgId);
        String spaceImgUrl = findSpaceImg.get().getSpaceImgUrl();
        try {
            amazonS3.deleteObject(this.bucket, spaceImgUrl.substring(spaceImgUrl.lastIndexOf("/")));
        } catch (AmazonServiceException e){
            log.error(e.getErrorMessage());
        }
    }


}
