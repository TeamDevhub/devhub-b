package teamdevhub.devhub.adapter.out.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import teamdevhub.devhub.adapter.out.exception.ExternalServiceException;
import teamdevhub.devhub.common.enums.ErrorCode;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Adapter {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        String fileName = generateFileName(multipartFile.getOriginalFilename());

        try (InputStream inputStream = multipartFile.getInputStream()) {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(multipartFile.getContentType())
                    .contentLength(multipartFile.getSize())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(inputStream, multipartFile.getSize())
            );
            return generatePublicUrl(fileName);

        } catch (IOException e) {
            throw ExternalServiceException.of(ErrorCode.UNKNOWN_FAIL);
        }
    }

    public void delete(String imageUrl) {
        if (!StringUtils.hasText(imageUrl)) return;

        String objectKey = extractObjectKey(imageUrl);

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .build());
        } catch (Exception e) {
            throw ExternalServiceException.of(ErrorCode.UNKNOWN_FAIL);
        }
    }

    private String generateFileName(String originalFilename) {
        if (!StringUtils.hasText(originalFilename)) {
            throw ExternalServiceException.of(ErrorCode.UNKNOWN_FAIL);
        }

        String extension = extractExtension(originalFilename);
        return UUID.randomUUID() + "." + extension;
    }

    private String extractExtension(String filename) {
        int idx = filename.lastIndexOf(".");
        if (idx == -1) {
            throw ExternalServiceException.of(ErrorCode.UNKNOWN_FAIL);
        }
        return filename.substring(idx + 1);
    }

    private String extractObjectKey(String imageUrl) {
        try {
            return new URL(imageUrl).getPath().substring(1);
        } catch (Exception e) {
            throw ExternalServiceException.of(ErrorCode.UNKNOWN_FAIL);
        }
    }

    private String generatePublicUrl(String key) {
        return "https://" + bucket + ".s3.amazonaws.com/" + key;
    }
}

