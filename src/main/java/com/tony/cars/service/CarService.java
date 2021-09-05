package com.tony.cars.service;

import com.tony.cars.domain.Car;
import com.tony.cars.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars() {
        List<Car> cars = carRepository.findAll();

        return cars;
    }

    public Optional<Car> findByCar(Long id) {
        Optional<Car> car = carRepository.findById(id);

        return car;
    }
}
