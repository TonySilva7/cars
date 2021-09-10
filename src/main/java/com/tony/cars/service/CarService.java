package com.tony.cars.service;

import com.tony.cars.domain.Car;
import com.tony.cars.domain.dto.CarDTO;
import com.tony.cars.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    public CarDTO saveCar(Car car) {
        if (car.equals(null)) {
            throw new IllegalArgumentException("Algo deu errado!");
        }

        CarDTO carDTO = CarDTO.toDTO(carRepository.save(car)) ;
        Optional<CarDTO> myCar = Optional.of(carDTO);
        return myCar.orElseThrow(() -> new IllegalArgumentException("Algo deu errado!"));
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

            Car obj = CarDTO.toEntity(c);
            carRepository.save(obj);

            return c;
        }).orElseThrow(() -> new IllegalArgumentException("Argumentos enviados são inválidos"));

        return carDB;
    }

    public boolean deleteCar(Long id) {
        Optional<CarDTO> car = findCarById(id);

        if (car.isPresent()) {
            carRepository.deleteById(id);
            return true;
        } else {
            return false;
            //throw new IllegalArgumentException("Algo deu errado!");
        }
    }
}
