package com.tony.cars.service;

import com.tony.cars.domain.Car;
import com.tony.cars.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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

    public Optional<Car> findCarById(Long id) {
        Optional<Car> car = carRepository.findById(id);

        return car;
    }

    public List<Car> findCarByType(String type) {
        List<Car> car = carRepository.findByType(type);

        return car;
    }

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    public Car updateCar(Long id, Car car) {
        Assert.notNull(id, "Não foi possível atualizar o registro!");

        /*
        Optional<Car> carDB = findCarById(id);

        if (carDB.isPresent()) {
            Car currentCar = carDB.get();

            currentCar.setName(car.getName());
            currentCar.setDescription(car.getDescription());
            currentCar.setUrlPhoto(car.getUrlPhoto());
            currentCar.setUrlVideo(car.getUrlVideo());
            currentCar.setLatitude(car.getLatitude());
            currentCar.setLongitude(car.getLongitude());
            currentCar.setType(car.getType());

            carRepository.save(currentCar);
            return currentCar;
        } else {
            throw new RuntimeException("Não foi possível atualizar");
        }
         */

        Car myCar = findCarById(id).map((currentCar) -> {
            currentCar.setName(car.getName());
            currentCar.setDescription(car.getDescription());
            currentCar.setUrlPhoto(car.getUrlPhoto());
            currentCar.setUrlVideo(car.getUrlVideo());
            currentCar.setLatitude(car.getLatitude());
            currentCar.setLongitude(car.getLongitude());
            currentCar.setType(car.getType());

            return carRepository.save(currentCar);
        }).orElseThrow(() -> new RuntimeException("Não foi possível atualizar"));

        return myCar;
    }

    public void deleteCar(Long id) {
        Optional<Car> car = findCarById(id);

        if(car.isPresent()) {
            carRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Algo deu errado!");
        }
    }
}
