package com.lenovo.training.core.controller;

import com.lenovo.training.core.entity.Device;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class DeviceControllerTest extends ControllerTestBaseClass {

    private static final String DEVICES = "/devices";
    private static final String SERIES = "/devices/series";
    private static final String DEVICES_ALL = "/devices/all";

    private final static String REQUEST_BODY = "{\n"
        + "\"serialNumber\": \"Iphone\",\n"
        + "\"model\": \"SE2\",\n"
        + "\"description\": \"April 24, 2020\"\n"
        + "}";

    private final static String FIRST_DEVICE_SERIAL_NUMBER = "Ip-7";
    private final static String SECOND_DEVICE_SERIAL_NUMBER = "Ip-8";

    Device firstDevice = new Device(FIRST_DEVICE_SERIAL_NUMBER, "Iphone",
        "Iphone 7");
    Device secondDevice = new Device(SECOND_DEVICE_SERIAL_NUMBER, "Iphone",
        "Iphone 8");

    @BeforeEach
    void setUp() {
        Mockito.when(accessToken.getPreferredUsername()).thenReturn("shakhno");
        Mockito.when(authService.getToken()).thenReturn(accessToken);
    }

    @AfterEach
    void clean() {
        mongoTemplate.dropCollection(Device.class);
    }

    @Test
    @DisplayName("GET request to \"/devices/all\" endpoint should get all devices")
    void getRequestShouldReturnAllDevices() throws Exception {

        mongoTemplate.insert(firstDevice);
        mongoTemplate.insert(secondDevice);

        getResponse(HttpMethod.GET, DEVICES_ALL, "", HttpStatus.OK)
            .andExpect(jsonPath("$[0].serialNumber").value(FIRST_DEVICE_SERIAL_NUMBER))
            .andExpect(jsonPath("$[1].serialNumber").value(SECOND_DEVICE_SERIAL_NUMBER));
    }

    @Nested
    @DisplayName("POST request to \"/devices\" endpoint should ")
    class AddDeviceTest {

        @Test
        @DisplayName("create a new device with provided data")
        void postShouldCreateADevice() throws Exception {

            getResponse(HttpMethod.POST, DEVICES, REQUEST_BODY, HttpStatus.CREATED);

            List<Device> allDevices = mongoTemplate.findAll(Device.class);

            assertEquals(1, allDevices.size());
        }

        @Test
        @DisplayName("response with bad request status when content is empty")
        void postShouldResponseBadRequestStatus() throws Exception {

            getResponse(HttpMethod.POST, DEVICES, "{\n"
                    + "\"\": \"\",\n"
                    + "\"\": \"\",\n"
                    + "\"\": \"\"\n"
                    + "}",
                HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("throw ResourceExistsException when device serial number already exist")
        void postDeviceWithExistingSerialNumberShouldThrowResourceExistsException()
            throws Exception {

            mongoTemplate.insert(firstDevice);

            String duplicateDevice = "{\n"
                + "\"serialNumber\": \"Ip-7\",\n"
                + "\"model\": \"Iphone\",\n"
                + "\"description\": \"Iphone 7\"\n"
                + "}";

            getResponse(HttpMethod.POST, DEVICES, duplicateDevice, HttpStatus.BAD_REQUEST);

            List<Device> allDevices = mongoTemplate.findAll(Device.class);

            assertEquals(1, allDevices.size());
        }

        @Test
        @DisplayName("throw ResourceExistsException when device serial number already exist")
        void postDeviceWithInvalidCharactersShouldThrowMethodArgumentNotValidException()
            throws Exception {

            String deviceWithInvalidCharacters = "{\n"
                + "\"serialNumber\": \"Iphone.\",\n"
                + "\"model\": \"XR-1\",\n"
                + "\"description\": \"May 3, 2018\"\n"
                + "}";

            getResponse(HttpMethod.POST, DEVICES, deviceWithInvalidCharacters,
                HttpStatus.BAD_REQUEST);
            List<Device> allDevices = mongoTemplate.findAll(Device.class);
            assertEquals(0, allDevices.size());
        }
    }

    @Nested
    @DisplayName("GET request to \"/devices/{id}\" endpoint should ")
    class GetDeviceByIdTest {

        @Test
        @DisplayName("get a device specified by \"id\"")
        void getByIdRequestShouldReturnDevice() throws Exception {

            mongoTemplate.insert(firstDevice);
            mongoTemplate.insert(secondDevice);

            String id = mongoTemplate.findAll(Device.class).get(0).getId();

            getResponse(HttpMethod.GET, DEVICES + "/" + id, "", HttpStatus.OK)
                .andExpect(jsonPath("$.serialNumber").value(FIRST_DEVICE_SERIAL_NUMBER));
        }

        @Test
        @DisplayName("throw ResourceNotFoundException when device is not exist")
        void getByIdRequestShouldThrowResourceNotFoundException() throws Exception {
            getResponse(HttpMethod.GET, DEVICES + "/incorrectId", "",
                HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("GET request to \"/devices/series/{serialNumber}\" endpoint should ")
    class GetDeviceBySerialNumberTest {

        @Test
        @DisplayName("get a device specified by \"serialNumber\"")
        void getBySerialNumberRequestShouldReturnDevice() throws Exception {

            mongoTemplate.insert(firstDevice);
            mongoTemplate.insert(secondDevice);

            String serialNumber = mongoTemplate.findAll(Device.class).get(0).getSerialNumber();

            getResponse(HttpMethod.GET, SERIES + "/" + serialNumber, "", HttpStatus.OK)
                .andExpect(jsonPath("$.serialNumber").value(FIRST_DEVICE_SERIAL_NUMBER));
        }

        @Test
        @DisplayName("throw ResourceNotFoundException when device is not exist")
        void getBySerialNumberRequestShouldThrowResourceNotFoundException() throws Exception {
            getResponse(HttpMethod.GET, SERIES + "/incorrectSerialNumber", "",
                HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("GET request to \"/devices/all?model=\" endpoint should ")
    class GetDevicesByModelTest {

        @Test
        @DisplayName("get all devices specified by \"model\"")
        void getAllByModelRequestShouldReturnDevice() throws Exception {

            mongoTemplate.insert(firstDevice);
            mongoTemplate.insert(secondDevice);

            getResponse(HttpMethod.GET, DEVICES_ALL + "?model=Iphone", "", HttpStatus.OK)
                .andExpect(jsonPath("$[0].serialNumber").value(FIRST_DEVICE_SERIAL_NUMBER))
                .andExpect(jsonPath("$[1].serialNumber").value(SECOND_DEVICE_SERIAL_NUMBER));
        }
    }

    @Nested
    @DisplayName("DELETE request to \"/devices/{id}\" endpoint should ")
    class DeleteDeviceTest {

        @Test
        @DisplayName("remove device with given id")
        void deleteRequestShouldRemoveDeviceWithSpecifiedId() throws Exception {

            mongoTemplate.insert(firstDevice);
            mongoTemplate.insert(secondDevice);

            String id = mongoTemplate.findAll(Device.class).get(0).getId();

            getResponse(HttpMethod.DELETE, DEVICES + "/" + id, "",
                HttpStatus.NO_CONTENT)
                .andExpect(jsonPath("$").doesNotExist());

            List<Device> allDevices = mongoTemplate.findAll(Device.class);

            assertEquals(1, allDevices.size());
            assertEquals(SECOND_DEVICE_SERIAL_NUMBER, allDevices.get(0).getSerialNumber());
        }

        @Test
        @DisplayName("throw ResourceNotFoundException when device is not exist")
        void deleteRequestShouldThrowResourceNotFoundException() throws Exception {
            getResponse(HttpMethod.DELETE, DEVICES + "/incorrectId", "",
                HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("PUT request to \"/devices/{id}\" endpoint should ")
    class UpdateDeviceTest {

        @Test
        @DisplayName("update specified by \"id\" device")
        void putDeviceRequestShouldUpdateSpecifiedDevice() throws Exception {

            String editedSerialNumber = "Edited serial number";

            mongoTemplate.insert(firstDevice);
            mongoTemplate.insert(secondDevice);

            String id = mongoTemplate.findAll(Device.class).get(0).getId();
            String RequestBody = "{\n"
                + "\"serialNumber\":\"" + editedSerialNumber + "\",\n"
                + "\"model\": \"Iphone\",\n"
                + "\"description\": \"Iphone 8\"\n"
                + "}";

            getResponse(HttpMethod.PUT, DEVICES + "/" + id, RequestBody, HttpStatus.OK);

            List<Device> allDevices = mongoTemplate.findAll(Device.class);

            assertEquals(2, allDevices.size());
            assertEquals(editedSerialNumber, allDevices.get(0).getSerialNumber());
            assertEquals(SECOND_DEVICE_SERIAL_NUMBER, allDevices.get(1).getSerialNumber());
        }

        @Test
        @DisplayName("throw ResourceNotFoundException when incorrect device id")
        void putDeviceRequestShouldThrowResourceNotFoundException() throws Exception {

            String id = "incorrectId";

            String RequestBody = "{\n"
                + "\"id\":\"" + id + "\""
                + "\"serialNumber\": \"Iphone\",\n"
                + "\"model\": \"SE2\",\n"
                + "\"description\": \"April 24, 2020\"\n"
                + "}";

            getResponse(HttpMethod.PUT, DEVICES + "/" + id, RequestBody,
                HttpStatus.BAD_REQUEST);
        }
    }
}
