package com.tony.cars.api;

import com.tony.cars.domain.Car;
import com.tony.cars.domain.dto.CarDTO;
import com.tony.cars.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<CarDTO>> getAllCars() {
        List<CarDTO> cars = carService.getAllCars();

        return ResponseEntity.ok().body(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> findCarById(@PathVariable Long id) {
        CarDTO car = carService.findCarById(id);
        return ResponseEntity.ok().body(car);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<CarDTO>> findCarByType(@PathVariable String type) {
        List<CarDTO> cars = carService.findCarByType(type);

        return ResponseEntity.ok().body(cars);
    }

    // retorna uri do novo recurso
    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<CarDTO> insertCar(@RequestBody Car car) {
            CarDTO myCar = carService.saveCar(car);
            URI uriLocation = getUri(myCar.getId());

            return ResponseEntity.created(uriLocation).body(myCar);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CarDTO> updateCar(@PathVariable Long id, @RequestBody Car car) {
        car.setId(id);
        CarDTO myCar = carService.updateCar(id, car);

        return ResponseEntity.ok().body(myCar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

}
