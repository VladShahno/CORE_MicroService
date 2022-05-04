package com.lenovo.training.core.service;

import com.lenovo.training.core.payload.DeviceRequest;
import java.util.List;

public interface KafkaConsumerService {

    void consume(List<DeviceRequest> deviceRequests);
}
