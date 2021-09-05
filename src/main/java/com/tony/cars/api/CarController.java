package com.tony.cars.api;

import com.tony.cars.domain.Car;
import com.tony.cars.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping()
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();

        return ResponseEntity.ok().body(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Car>> getAllCars(@PathVariable Long id) {
        Optional<Car> car = carService.findByCar(id);

        return ResponseEntity.ok().body(car);
    }

}
