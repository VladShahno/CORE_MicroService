package com.lenovo.training.core.controller;

import com.lenovo.training.core.service.MinIoService;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class MinIoController {

    private MinIoService minioService;

    /*@GetMapping("/")
    public List<Item> testMinio() throws MinioException {
        return minioService.list();
    }*/

    @PostMapping
    public void upload() throws ServerException, InsufficientDataException, ErrorResponseException,
        NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidResponseException,
        XmlParserException, InternalException {
        minioService.uploadObject();
    }
}
