package com.lenovo.training.core.service;

import com.lenovo.training.core.payload.DeviceRequest;
import java.util.List;

public interface ConsumerService {

    void consume(List<DeviceRequest> dto);
}
