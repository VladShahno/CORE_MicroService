package com.lenovo.training.core.controller;

import com.lenovo.training.core.entity.Device;
import com.lenovo.training.core.payload.DeviceRequest;
import com.lenovo.training.core.service.impl.DeviceServiceImpl;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("devices")
@CrossOrigin(origins = "http://localhost:8081/", maxAge = 3600)
@AllArgsConstructor
@Validated
public class DeviceController {

    private final DeviceServiceImpl deviceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @ApiOperation("Adds a new Device. Throws an exception if the Serial number is not unique.")
    public Device createDevice(@Valid @RequestBody DeviceRequest deviceRequest) {
        return deviceService.addDevice(deviceRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation("Updates a Device. Throws an exception if the required id does not exist or "
        + "serial number already exist.")
    public Device updateDevice(@Valid @RequestBody DeviceRequest deviceRequest,
                               @PathVariable
                               @NotBlank(message = "{not.blank}") String id) {
        return deviceService.updateDevice(id, deviceRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes a Device. Throws an exception if the required id does not exist.")
    public void deleteDevice(@PathVariable @NotBlank(message = "{not.blank}") String id) {
        deviceService.deleteDeviceById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Returns all Devices or all Devices by model if model is provided. Return an "
        + "empty List if the required model does not exist")
    public List<Device> getAllDevices(
        @RequestParam(required = false, defaultValue = "") String model) {
        return deviceService.getAllDevices(model);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Returns a Device by id. Throws an exception if the required id does not exist.")
    public Device getDeviceById(@PathVariable @NotBlank(message = "{not.blank}") String id) {
        return deviceService.getById(id);
    }

    @GetMapping("/series/{serialNumber}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Returns a Device by serial number. Throws an exception if the required serial"
        + " number does not exist.")
    public Device getDeviceBySerialNumber(
        @PathVariable @NotBlank(message = "{not.blank}") String serialNumber) {
        return deviceService.getBySerialNumber(serialNumber);
    }
}