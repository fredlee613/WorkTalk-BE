package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.SpaceImg;
import com.golfzonTech4.worktalk.repository.SpaceImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class SpaceImgService {

    @Value("${spaceImgLocation}")
    private String spaceImgLocation;

    private final SpaceImgRepository spaceImgRepository;

    private final FileService fileService;

    public void saveSpaceImg(SpaceImg spaceImg, MultipartFile multipartFile) throws Exception{
        String oriImgName = multipartFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(spaceImgLocation, imgName,
                    multipartFile.getBytes());
            imgUrl = "/images/space/" + imgName;
        }

        //상품 이미지 정보 저장
//        spaceImgg.updateSpaceImg(imgName,imgUrl);
        spaceImgRepository.save(spaceImg);
    }

    public void updateSpaceImg(Long spaceImgId, MultipartFile multipartFile) throws Exception{
        if(!multipartFile.isEmpty()){
            SpaceImg savedItemImg = spaceImgRepository.findById(spaceImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(spaceImgLocation+"/"+
                        savedItemImg.getImgName());
            }

            String oriImgName = multipartFile.getOriginalFilename();
            String imgName = fileService.uploadFile(spaceImgLocation, oriImgName, multipartFile.getBytes());
            String imgUrl = "/images/space/" + imgName;
//            savedItemImg.updateSpaceImg(imgName,imgUrl);
        }
    }

}