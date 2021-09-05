package com.tony.cars.api;

import com.tony.cars.domain.Car;
import com.tony.cars.service.CarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping()
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

}
