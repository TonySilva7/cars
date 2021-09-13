package com.tony.cars.service.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(Object id) {
        super("Resource not found: " + id);
    }
}
