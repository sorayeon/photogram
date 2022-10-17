package com.cos.photogramstart.service.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cos.photogramstart.handler.ex.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AmazonS3ResourceStorage {
    private AmazonS3 amazonS3Client;
    private String BASE_DIR = "photogram/upload";

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        amazonS3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public String store(MultipartFile multipartFile) {
        UUID fileName = UUID.randomUUID();
        String extension = getExtension(multipartFile.getContentType());
        String imageFileName = String.format("%s/%s.%s", BASE_DIR, fileName, extension);
        File file = null;
        try {
            //S3에 전달할 수 있도록 MultiPartFile 을 File 로 전환
            file = convert(multipartFile)
                    .orElseThrow(() -> new CustomException("File 로 전환이 실패했습니다."));

            //전환된 File을 S3에 public 읽기 권한으로 put
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, imageFileName, file)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            //업로드된 파일의 S3 URL 주소를 반환
            return amazonS3Client.getUrl(bucket, imageFileName).toString();
        } catch (IOException e) {
            throw new CustomException("파일을 s3에 업로드 중 오류가 발생하였습니다.");
        } finally {
            //로컬에 생성된 File 삭제
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    private Optional<File> convert(MultipartFile multipartFile) throws IOException {
        File convertFile = new File(multipartFile.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    public String getExtension(String contentType) {
        if (StringUtils.hasText(contentType)) {
            return contentType.substring(contentType.lastIndexOf('/') + 1);
        }
        return null;
    }
}
