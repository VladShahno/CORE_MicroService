package com.lenovo.training.core.service;

import com.lenovo.training.core.entity.Device;
import com.lenovo.training.core.payload.DeviceRequest;
import java.util.List;

public interface DeviceService {

    Device addDevice(DeviceRequest deviceRequest);

    void deleteDeviceById(String id);

    Device updateDevice(String id, DeviceRequest deviceRequest);

    Device getById(String id);

    Device getBySerialNumber(String serialNumber);

    List<Device> getAllDevices(String model);

    void addDevicesList(List<DeviceRequest> devices);

    List<Device> getAllDevicesByCurrentDay();
}
