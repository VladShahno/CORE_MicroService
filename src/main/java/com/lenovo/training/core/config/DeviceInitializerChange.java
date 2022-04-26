package com.lenovo.training.core.config;

import com.lenovo.training.core.payload.DeviceRequest;
import com.lenovo.training.core.service.DeviceService;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.AllArgsConstructor;

@ChangeUnit(id = "device-initializer", order = "1", author = "vshakhno")
@AllArgsConstructor
public class DeviceInitializerChange {
    private final DeviceService deviceService;

    @Execution
    public void changeSet() {
        deviceService.addDevice(new DeviceRequest("C36B7811", "Z2XQ",
            "ThinkStation P620"));
        deviceService.addDevice(new DeviceRequest("246277C3", "20HA",
            "ThinkPad T410"));
        deviceService.addDevice(new DeviceRequest("5623-KU", "GalaxyS9",
            "Samsung Galaxy S9"));
    }

    @RollbackExecution
    public void rollback() {
    }
}