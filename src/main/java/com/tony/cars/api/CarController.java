package com.tony.cars.api;

import com.tony.cars.domain.Car;
import com.tony.cars.service.CarService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Optional<Car>> findCarById(@PathVariable Long id) {
        Optional<Car> car = carService.findCarById(id);

        return ResponseEntity.ok().body(car);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Car>> findCarByType(@PathVariable String type) {
        List<Car> cars = carService.findCarByType(type);

        return ResponseEntity.ok().body(cars);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Car> insertCar(@RequestBody Car car) {
        Car myCar = carService.saveCar(car);

        return ResponseEntity.ok().body(myCar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        Car myCar = carService.updateCar(id, car);

        return ResponseEntity.ok().body(myCar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {

        try {
            carService.deleteCar(id);
        } catch (IllegalArgumentException e) {
            System.out.println("Meu Erro: " + e);
            // e.printStackTrace();
        }

        return ResponseEntity.noContent().build();
    }

}
