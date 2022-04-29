package com.lenovo.training.core.service.impl;

import com.lenovo.training.core.service.MinIoService;
import com.lenovo.training.core.util.common.MinIoProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MinIoServiceImpl implements MinIoService {

    private static final Logger LOGGER = LoggerFactory.getLogger("MinioServiceImpl");

    private final MinioClient minioClient;
    private MinIoProperties minIoProperties;


    @Override
    public void uploadObject() {
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(minIoProperties.getBucketName())
                .object("test.csv")
                .stream()
                .contentType()
                .build());
    }
}
