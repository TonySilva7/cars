package com.tony.cars.api;

import com.tony.cars.domain.Car;
import com.tony.cars.domain.dto.CarDTO;
import com.tony.cars.service.CarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Api(value = "Carros")
@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @ApiOperation(value = "Deve retornar uma lista de carros")
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

    // retorna uri do novo recurso
    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CarDTO> insertCar(@RequestBody Car car) {
        try {
            CarDTO myCar = carService.saveCar(car);
            URI uriLocation = getUri(myCar.getId());

            return ResponseEntity.created(uriLocation).body(myCar);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Optional<CarDTO>> updateCar(@PathVariable Long id, @RequestBody Car car) {

        car.setId(id);
        Optional<CarDTO> myCar = carService.updateCar(id, car);

        return myCar.isPresent() ? ResponseEntity.ok().body(myCar) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        boolean ok = carService.deleteCar(id);
        return ok ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
    }

}
