package com.tony.cars.service;

import com.tony.cars.domain.Car;
import com.tony.cars.domain.dto.CarDTO;
import com.tony.cars.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<CarDTO> getAllCars() {
        List<Car> cars = carRepository.findAll();

        List<CarDTO> listCars = cars.stream().map((c) -> CarDTO.toDTO(c)).collect(Collectors.toList());

        return listCars;
    }

    public Optional<CarDTO> findCarById(Long id) {
        return carRepository.findById(id).map((c) -> CarDTO.toDTO(c));
        /*
        Optional<Car> car = carRepository.findById(id);
        return car.map((c) -> Optional.of(new CarDTO(c))).orElse(null);
        */
    }

    public List<CarDTO> findCarByType(String type) {
        List<Car> cars = carRepository.findByType(type);
        List<CarDTO> listCars = cars.stream().map(CarDTO::toDTO).collect(Collectors.toList());

        return listCars;
    }

    public Optional<CarDTO> saveCar(Car car) {
        Car myCar = carRepository.save(car);
        return Optional.of(CarDTO.toDTO(myCar));
    }

    public Optional<CarDTO> updateCar(Long id, Car car) {
        Assert.notNull(id, "Não foi possível atualizar o registro!");

        Optional<CarDTO> carDB = findCarById(id);

        carDB.map((c) -> {
            c.setName(car.getName());
            c.setDescription(car.getDescription());
            c.setUrlPhoto(car.getUrlPhoto());
            c.setUrlVideo(car.getUrlVideo());
            c.setLatitude(car.getLatitude());
            c.setLongitude(car.getLongitude());
            c.setType(car.getType());

            return carRepository.save(CarDTO.toEntity(c));
        }).orElseThrow(() -> new RuntimeException("Não foi possível atualizar"));

        return carDB;
    }

    public void deleteCar(Long id) {
        Optional<CarDTO> car = findCarById(id);

        if (car.isPresent()) {
            carRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Algo deu errado!");
        }
    }
}
