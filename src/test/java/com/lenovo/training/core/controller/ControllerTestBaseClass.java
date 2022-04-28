package com.lenovo.training.core.controller;

import com.lenovo.training.core.service.AuthService;
import org.junit.jupiter.api.TestInstance;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class ControllerTestBaseClass {

    @Autowired
    public MockMvc mockMvc;
    @Autowired
    public MongoTemplate mongoTemplate;
    @Autowired
    public AuthService authService;
    @Autowired
    public AccessToken accessToken;

    public ResultActions getResponse(HttpMethod method, String endpoint, String contentData,
                                     HttpStatus httpStatus) throws Exception {

        return mockMvc.perform(MockMvcRequestBuilders
                .request(HttpMethod.valueOf(method.name()), endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentData))
            .andExpect(status().is(httpStatus.value()));
    }
}
