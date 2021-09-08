package com.tony.cars.api;

import com.tony.cars.domain.Car;
import com.tony.cars.domain.dto.CarDTO;
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

    /*
    @GetMapping()
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();

        return ResponseEntity.ok().body(cars);
    }
     */
    @GetMapping()
    public ResponseEntity<List<CarDTO>> getAllCars() {
        List<CarDTO> cars = carService.getAllCars();

        return ResponseEntity.ok().body(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> findCarById(@PathVariable Long id) {
        Optional<CarDTO> car = carService.findCarById(id);

        return car.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<CarDTO>> findCarByType(@PathVariable String type) {
        List<CarDTO> cars = carService.findCarByType(type);

        return cars.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok().body(cars);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Optional<CarDTO>> insertCar(@RequestBody Car car) {
        Optional<CarDTO> myCar = carService.saveCar(car);

        return ResponseEntity.ok().body(myCar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<CarDTO>> updateCar(@PathVariable Long id, @RequestBody Car car) {
        Optional<CarDTO> myCar = carService.updateCar(id, car);

        return ResponseEntity.ok().body(myCar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {

        try {
            carService.deleteCar(id);
        } catch (IllegalArgumentException e) {
            System.out.println("Meu Erro: " + e);
            return ResponseEntity.notFound().build();
            // e.printStackTrace();
        }

        return ResponseEntity.noContent().build();
    }

}
