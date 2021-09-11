package com.tony.cars;

import com.tony.cars.domain.Car;
import com.tony.cars.domain.dto.CarDTO;
import com.tony.cars.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarsServiceTest {

    @Autowired
    private CarService carService; // instancia classe

    @Test
    void contextLoads() {
    }

    // Testa inserção, remoção e tipo do objeto
    @Test
    public void testInsert() {
        Car car = new Car(
                null,
                "Ferrari 2",
                "Carro foda",
                "http://minha-url.com",
                "http://minha-url.com",
                "-123454",
                "-98432",
                "classico"
        );

        CarDTO carDTO = carService.saveCar(car);
        assertNotNull(carDTO);

        Long id = carDTO.getId();
        assertNotNull(id);

        // busca Object
        Optional<CarDTO> op = carService.findCarById(id);
        assertTrue(op.isPresent());

        carDTO = op.get();
        assertEquals("Ferrari 2", carDTO.getName());
        assertEquals("classico", carDTO.getType());

        // deleta objeto
        carService.deleteCar(id);

        // verifica se realmente deletou
        assertFalse(carService.findCarById(id).isPresent());

    }

    // Testa a list de carros
    @Test
    public void testCarLists() {
        List<CarDTO> cars = carService.getAllCars();
        assertEquals(30, cars.size());
    }

    @Test
    public void testListaPorTipo() {
        assertEquals(10, carService.findCarByType("classicos").size());
        assertEquals(10, carService.findCarByType("esportivos").size());
        assertEquals(10, carService.findCarByType("luxo").size());

        assertEquals(0, carService.findCarByType("x").size());
    }

    @Test
    public void testGetOnlyCar() {
        Optional<CarDTO> op = carService.findCarById(11L);
        assertTrue(op.isPresent());

        CarDTO c = op.get();
        assertEquals("Ferrari FF", c.getName());
    }
}