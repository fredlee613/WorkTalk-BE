package com.golfzonTech4.worktalk.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.golfzonTech4.worktalk.domain.Space;
import com.golfzonTech4.worktalk.repository.space.SpaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public List<String> upload(List<MultipartFile> multipartFileList) {
        List<String> imageUrlList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFileList) {
            String originalName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();// UUID + 파일이름

            ObjectMetadata objMeta = new ObjectMetadata(); //S3에 파일사이즈 알려주기 위해 ObjectMetadata 사용
            objMeta.setContentType(multipartFile.getContentType());
            objMeta.setContentLength(multipartFile.getSize()); // 파일 크기

            try (InputStream inputStream = multipartFile.getInputStream()) {
                // putObject(S3 API 메소드)를 이용해 파일 Stream 열어서 S3에 업로드
                amazonS3.putObject(
                        new PutObjectRequest(bucket, originalName, inputStream, objMeta)
                                .withCannedAcl(CannedAccessControlList.PublicRead));
                String imageUrl = amazonS3.getUrl(bucket, originalName).toString(); // getUrl메소드로 S3에 업로드된 사진 url 가져오기
                imageUrlList.add(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageUrlList;
    }

}