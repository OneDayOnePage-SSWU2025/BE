package com.example.odop.service;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
public class GCPService {

    private final Storage storage;

    @Value("${gcp.bucket-name}")
    private String bucketName;

    public GCPService(
            @Value("${gcp.project-id}") String projectId,
            @Value("${gcp.credentials.file}") String credentialsPath) {

        try {
            InputStream credStream;
            if (credentialsPath.startsWith("classpath:")) {
                String cp = credentialsPath.substring("classpath:".length());
                credStream = new ClassPathResource(cp).getInputStream();
            } else {
                credStream = java.nio.file.Files.newInputStream(java.nio.file.Path.of(credentialsPath));
            }

            this.storage = StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(ServiceAccountCredentials.fromStream(credStream))
                    .build()
                    .getService();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize GCP Storage: " + e.getMessage(), e);
        }
    }

    // 업로드 (폴더명/랜덤UUID_파일명)
    public String upload(MultipartFile multipartFile, String dirName) {
        try {
            String originalFileName = multipartFile.getOriginalFilename();
            if (originalFileName == null) originalFileName = "file";
            String fileName = dirName + "/" + UUID.randomUUID() + "_" + originalFileName.replaceAll("\\s+", "_");

            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                    .setContentType(multipartFile.getContentType())
                    .build();

            storage.create(blobInfo, multipartFile.getBytes());
            log.info("Uploaded to GCS: gs://{}/{}", bucketName, fileName);

            return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload to GCS: " + e.getMessage(), e);
        }
    }

    // 파일 삭제
    public void deleteFile(String fileNameOrUrl) {
        try {
            String decoded = URLDecoder.decode(fileNameOrUrl, StandardCharsets.UTF_8);
            String objectName = extractObjectName(decoded);

            boolean deleted = storage.delete(BlobId.of(bucketName, objectName));
            if (deleted) {
                log.info("Deleted GCS object: {}", objectName);
            } else {
                log.warn("No object found for deletion: {}", objectName);
            }
        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage());
        }
    }

    // 파일 수정
    public String updateFile(MultipartFile newFile, String oldFile, String dirName) {
        log.info("Updating file in GCS: {}", oldFile);
        deleteFile(oldFile);
        return upload(newFile, dirName);
    }

    // URL → object name 변환
    private String extractObjectName(String url) {
        if (url.contains(bucketName)) {
            int idx = url.indexOf(bucketName) + bucketName.length() + 1;
            return url.substring(idx);
        }
        return url;
    }
}
