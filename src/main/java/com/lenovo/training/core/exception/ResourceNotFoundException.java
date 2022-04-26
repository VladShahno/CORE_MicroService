package com.lenovo.training.core.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Resource not found");
    }

    public ResourceNotFoundException(String resource) {
        super(String.format("%s not found", resource));
    }

    public ResourceNotFoundException(String typeOfResource, String resource) {
        super(String.format("%s with id: %s not found", typeOfResource, resource));
    }

    public ResourceNotFoundException(String typeOfResource, String identifier, String resource) {
        super(String.format("%s with %s: %s not found", typeOfResource, identifier, resource));
    }
}
