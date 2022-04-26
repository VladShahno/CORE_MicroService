package com.lenovo.training.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "devices")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    @Id
    private String id;

    private String serialNumber;

    private String model;

    private String description;

    public Device(String serialNumber, String model, String description) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.description = description;
    }
}
