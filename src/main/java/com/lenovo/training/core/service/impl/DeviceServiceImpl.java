package com.lenovo.training.core.service.impl;

import com.lenovo.training.core.entity.Device;
import com.lenovo.training.core.exception.ResourceExistsException;
import com.lenovo.training.core.exception.ResourceNotFoundException;
import com.lenovo.training.core.payload.DeviceRequest;
import com.lenovo.training.core.repository.DeviceRepository;
import com.lenovo.training.core.service.DeviceService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.lenovo.training.core.util.common.Constants.Logging.DEVICE;
import static com.lenovo.training.core.util.common.Constants.Logging.SERIAL_NUMBER;

@Service
@AllArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger("DeviceServiceImpl");

    private final DeviceRepository deviceRepository;

    @Override
    public Device addDevice(DeviceRequest deviceRequest) {
        if (deviceRepository.existsBySerialNumber(deviceRequest.getSerialNumber())) {
            LOGGER.error("Can't create device with existing serial number - " +
                deviceRequest.getSerialNumber());
            throw new ResourceExistsException(SERIAL_NUMBER + deviceRequest.getSerialNumber());
        }
        Device device = new Device();
        LOGGER.info("Creating device with serial number - " + deviceRequest.getSerialNumber());
        return saveDevice(device, deviceRequest);
    }

    @Override
    public void deleteDeviceById(String id) {
        if (deviceRepository.existsById(id)) {
            LOGGER.info("Deleting device with id - " + id);
            deviceRepository.deleteById(id);
        } else {
            LOGGER.error("No device with id - " + id);
            throw new ResourceNotFoundException(DEVICE, id);
        }
    }

    @Override
    public Device updateDevice(String id, DeviceRequest deviceRequest) {
        Device device = deviceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(DEVICE, id));
        if (deviceRepository.existsBySerialNumber(deviceRequest.getSerialNumber())
            && !deviceRequest.getSerialNumber().equals(device.getSerialNumber())) {
            LOGGER.error("Can't update device with existing serial number - " +
                deviceRequest.getSerialNumber());
            throw new ResourceExistsException(SERIAL_NUMBER + deviceRequest.getSerialNumber());
        }
        LOGGER.info("Updating device with id - " + id);
        return saveDevice(device, deviceRequest);
    }

    @Override
    public Device getById(String id) {
        LOGGER.info("Getting device with id - " + id);
        return deviceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(DEVICE, id));
    }

    @Override
    public Device getBySerialNumber(String serialNumber) {
        LOGGER.info("Getting device with serial number - " + serialNumber);
        return deviceRepository.findBySerialNumber(serialNumber)
            .orElseThrow(
                () -> new ResourceNotFoundException(DEVICE, SERIAL_NUMBER, serialNumber));
    }

    @Override
    public List<Device> getAllDevices(String model) {
        if (model.isEmpty()) {
            LOGGER.info("Getting all devices");
            return deviceRepository.findAll();
        }
        LOGGER.info("Getting all devices model - " + model);
        return deviceRepository.findAllByModel(model);
    }

    @Override
    public void addDevicesList(List<DeviceRequest> deviceRequestList) {

        List<Device> alreadyExist =
            deviceRepository.findBySerialNumberIn(deviceRequestList.stream().map(
                DeviceRequest::getSerialNumber).collect(Collectors.toList()));

        if (alreadyExist.isEmpty()) {
            LOGGER.info("Creating devices from List");
            saveDevicesList(getUniqueDeviceFromList(deviceRequestList));
        } else {
            LOGGER.error("Can't create device with existing serial number");
            throw new ResourceExistsException(SERIAL_NUMBER +
                Arrays.toString(alreadyExist.stream().map(
                    Device::getSerialNumber).toArray()));
        }
    }

    @Override
    public List<Device> getAllDevicesByCurrentDay() {
        LOGGER.info("Getting all devices for - " + LocalDate.now());
        return deviceRepository.findAllByCreationDate(LocalDate.now());
    }

    private Device saveDevice(Device device, DeviceRequest deviceRequest) {
        device.setSerialNumber(deviceRequest.getSerialNumber());
        device.setModel(deviceRequest.getModel());
        device.setDescription(deviceRequest.getDescription());
        return deviceRepository.save(device);
    }

    private List<Device> saveDevicesList(List<DeviceRequest> deviceRequestList) {
        return deviceRepository.saveAll(deviceRequestList.stream().map(
            deviceRequest -> new Device(deviceRequest.getSerialNumber(), deviceRequest.getModel(),
                deviceRequest.getDescription())).collect(Collectors.toList()));
    }

    private List<DeviceRequest> getUniqueDeviceFromList(List<DeviceRequest> deviceRequestList) {
        LOGGER.info("Checking for uniqueness of serial numbers from csv file...");
        return deviceRequestList.stream()
            .collect(Collectors.groupingBy(DeviceRequest::getSerialNumber))
            .values()
            .stream().flatMap(values -> values.stream().limit(1))
            .collect(Collectors.toList());
    }
}
