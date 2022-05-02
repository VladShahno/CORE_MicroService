package com.lenovo.training.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lenovo.training.core.service.DeviceService;
import com.lenovo.training.core.service.ExportService;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExportServiceImpl implements ExportService {

    private static final Logger LOGGER = LoggerFactory.getLogger("ExportServiceImpl");

    private ObjectMapper objectMapper;
    private DeviceService deviceService;

    public File writeDevicesToFileByCurrentDay() {
        try {
            File file = new File("[" + LocalDate.now() + "]" + "devices.json");

            LOGGER.info("Uploading all downloaded devices for the current day to a file");
            objectMapper.writeValue(file, deviceService.getAllDevicesByCurrentDay());
            return file;
        } catch (IOException e) {
            LOGGER.error("Error writing data to file!");
            throw new RuntimeException(e.getMessage());
        }
    }
}
