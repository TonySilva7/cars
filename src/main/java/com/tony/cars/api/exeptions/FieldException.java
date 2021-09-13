package com.tony.cars.api.exeptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Classe para tratar os campos da exceção
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FieldException {
    private String name;
    private String message;
}
