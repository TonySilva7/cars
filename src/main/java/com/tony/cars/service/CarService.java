package com.tony.cars.service;

import com.tony.cars.api.exeption.ObjectNotFoundException;
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

    public CarDTO findCarById(Long id) {
        return carRepository.findById(id)
                .map(CarDTO::toDTO)
                .orElseThrow(() -> new ObjectNotFoundException("Carro não encontrado!"));
    }

    public List<CarDTO> findCarByType(String type) {
        List<Car> cars = carRepository.findByType(type);
        List<CarDTO> listCars = cars.stream().map(CarDTO::toDTO).collect(Collectors.toList());

        return listCars;
    }

    public CarDTO saveCar(Car car) {
        Assert.isNull(car.getId(), "Não foi possível inserir o registro.");

        CarDTO carDTO = CarDTO.toDTO(carRepository.save(car));
        return carDTO;
        // Optional<CarDTO> myCar = Optional.of(carDTO);
        //return myCar.orElseThrow(() -> new IllegalArgumentException("Algo deu errado!"));
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
        }).orElseThrow(() -> new IllegalArgumentException("Argumentos enviados são inválidos"));

        return carDB;
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    /*
    public boolean deleteCar(Long id) {
        Optional<CarDTO> car = findCarById(id);

        if (car.isPresent()) {
            carRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
     */
}
