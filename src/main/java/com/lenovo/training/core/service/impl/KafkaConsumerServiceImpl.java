package com.lenovo.training.core.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lenovo.training.core.payload.DeviceRequest;
import com.lenovo.training.core.service.DeviceService;
import com.lenovo.training.core.service.KafkaConsumerService;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger("KafkaConsumerServiceImpl");

    private DeviceService deviceService;
    private ObjectMapper objectMapper;

    @Override
    @KafkaListener(id = "1", topics = "devices")
    public void consume(List<@Valid DeviceRequest> deviceRequests) {
        LOGGER.info("Consuming " + deviceRequests);
        List<DeviceRequest> requestsToSave = objectMapper.convertValue(deviceRequests,
            new TypeReference<>() {
            });
        deviceService.addDevicesList(requestsToSave);
    }
}
