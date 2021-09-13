package com.tony.cars.domain.exceptions;

import java.io.Serializable;

public class DomainException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public DomainException(String message) {
        super(message);
    }
}
