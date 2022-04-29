package com.lenovo.training.core.config;

import com.lenovo.training.core.util.common.MinIoProperties;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class MinIoConfig {

    private MinIoProperties minIoProperties;

    @Bean
    public MinioClient generateMinioClient() {
        return MinioClient.builder()
            .endpoint(minIoProperties.getUrl())
            .credentials(minIoProperties.getAccessKey(), minIoProperties.getAccessSecret())
            .build();
    }
}
