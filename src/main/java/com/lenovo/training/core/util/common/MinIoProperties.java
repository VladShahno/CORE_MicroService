package com.lenovo.training.core.util.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("minio")
@Data
public class MinIoProperties {

    String accessKey;
    String accessSecret;
    String url;
    String bucketName;
}
