package ktb.ayden.springboot.service;

import ktb.ayden.springboot.common.exception.CustomException;
import ktb.ayden.springboot.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ImageService {

    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.region}")
    private String region;

    public String upload(MultipartFile image, String directory) {
        validateImage(image);

        String key = createObjectKey(
                directory,
                image.getContentType()
        );

        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(image.getContentType())
                        .contentLength(image.getSize())
                        .build();

        try (InputStream inputStream = image.getInputStream()) {

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(
                            inputStream,
                            image.getSize()
                    )
            );

            return createPublicUrl(key);

        } catch (IOException | S3Exception e) {
            throw new CustomException(
                    ErrorCode.IMAGE_UPLOAD_FAILED
            );
        }
    }

    private void validateImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new CustomException(
                    ErrorCode.EMPTY_IMAGE
            );
        }

        if (image.getSize() > MAX_IMAGE_SIZE) {
            throw new CustomException(
                    ErrorCode.IMAGE_SIZE_EXCEEDED
            );
        }

        String contentType = image.getContentType();

        if (contentType == null ||
                !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new CustomException(
                    ErrorCode.INVALID_IMAGE_TYPE
            );
        }
    }

    private String createObjectKey(
            String directory,
            String contentType
    ) {
        String extension = resolveExtension(contentType);

        return directory
                + "/"
                + UUID.randomUUID()
                + "."
                + extension;
    }

    private String resolveExtension(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> throw new CustomException(
                    ErrorCode.INVALID_IMAGE_TYPE
            );
        };
    }

    private String createPublicUrl(String key) {
        return "https://"
                + bucket
                + ".s3."
                + region
                + ".amazonaws.com/"
                + key;
    }
}