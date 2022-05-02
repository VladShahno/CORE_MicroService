package com.lenovo.training.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lenovo.training.core.payload.DeviceRequest;
import com.lenovo.training.core.service.ConsumerService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ConsumerServiceImpl implements ConsumerService {
    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(id = "1", topics = "devices")
    public void consume(List<DeviceRequest> dto) {
        log.info("=> consumed {}", writeValueAsString(dto));
    }

    private String writeValueAsString(List<DeviceRequest> dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Writing value to JSON failed: " + dto.toString());
        }
    }
}
