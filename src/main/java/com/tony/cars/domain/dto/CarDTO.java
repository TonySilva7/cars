package com.tony.cars.domain.dto;

import com.tony.cars.domain.Car;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.io.Serializable;

@Data
public class CarDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private String urlPhoto;
    private String urlVideo;
    private String latitude;
    private String longitude;
    private String type;

    public static CarDTO toDTO(Car car) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(car, CarDTO.class);
    }

    public static Car toEntity(CarDTO carDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(carDTO, Car.class);
    }
}
