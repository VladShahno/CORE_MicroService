package com.lenovo.training.core.repository;

import com.lenovo.training.core.entity.Device;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {

    boolean existsBySerialNumber(String serialNumber);

    Optional<Device> findBySerialNumber(String serialNumber);

    List<Device> findAllByModel(String model);

    List<Device> findBySerialNumberIn(List<String> serialNumber);
}