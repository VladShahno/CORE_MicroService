package com.lenovo.training.core.service;

import org.keycloak.representations.AccessToken;

public interface AuthService {

    AccessToken getToken();
}
