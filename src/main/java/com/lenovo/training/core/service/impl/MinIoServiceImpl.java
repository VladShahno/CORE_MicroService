package com.lenovo.training.core.service.impl;

import com.lenovo.training.core.service.ExportService;
import com.lenovo.training.core.service.MinIoService;
import com.lenovo.training.core.util.common.MinIoProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import java.io.File;
import java.io.FileInputStream;
import lombok.AllArgsConstructor;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MinIoServiceImpl implements MinIoService {

    private static final Logger LOGGER = LoggerFactory.getLogger("MinioServiceImpl");

    private final MinioClient minioClient;
    private MinIoProperties minIoProperties;
    private ExportService exportService;

    @Override
    public void uploadFile() {

        File file = exportService.writeDevicesToFileByCurrentDay();
        createDefaultBucketIfNotExist();

        try (FileInputStream inputStream = new FileInputStream(file)) {
            minioClient.putObject(PutObjectArgs.builder()
                .bucket(minIoProperties.getBucket())
                .object(file.getName())
                .stream(inputStream, file.length(), -1)
                .contentType(String.valueOf(ContentType.TEXT_PLAIN))
                .build());
            LOGGER.info("Uploading file to bucket: " + minIoProperties.getBucket());
        } catch (Exception exception) {
            LOGGER.error("Error uploading file!");
            throw new RuntimeException(exception.getMessage());
        }
    }

    private void createDefaultBucketIfNotExist() {
        try {
            boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(minIoProperties.getBucket()).build());
            if (!found) {
                LOGGER.info("Creating bucket: " + minIoProperties.getBucket());
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(minIoProperties.getBucket()).build());
            }
        } catch (Exception e) {
            LOGGER.error("Error creating bucket: " + minIoProperties.getBucket());
            throw new RuntimeException(e.getMessage());
        }
    }
}
