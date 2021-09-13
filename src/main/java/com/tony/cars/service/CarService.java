package com.tony.cars.service;

import com.tony.cars.domain.Car;
import com.tony.cars.domain.dto.CarDTO;
import com.tony.cars.domain.exceptions.DomainException;
import com.tony.cars.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import javax.persistence.EntityNotFoundException;
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

    public CarDTO findCarById(Long id) {
        Optional<Car> myCar =  carRepository.findById(id);
        return myCar.map(CarDTO::toDTO).orElseThrow(() -> new EntityNotFoundException("Objeto não existe"));
    }

    public List<CarDTO> findCarByType(String type) {
        List<Car> cars = carRepository.findByType(type);

        if (cars.isEmpty()) {
            throw new DomainException("Necessita de um tipo válido");
        } else {
            return cars.stream().map(CarDTO::toDTO).collect(Collectors.toList());
        }

    }

    public CarDTO saveCar(Car car) {
        Assert.isNull(car.getId(), "Não foi possível inserir o registro.");

        return CarDTO.toDTO(carRepository.save(car));
    }

    public CarDTO updateCar(Long id, Car car) {
        Assert.notNull(id, "Não foi possível atualizar o registro!");

        CarDTO carDB = findCarById(id);


        Optional.of(carDB).map((c) -> {
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
        });

        return carDB;
    }

    public void deleteCar(Long id) {
        try {
            carRepository.deleteById(id);
        } catch (Exception e) {
            throw new DomainException("Necessita de um id válido");
        }
    }
}
