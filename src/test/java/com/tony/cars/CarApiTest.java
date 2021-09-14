package com.tony.cars;

import com.tony.cars.domain.Car;
import com.tony.cars.domain.dto.CarDTO;
import com.tony.cars.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = CarsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarApiTest {
    // -> Arrange ..................................................
    @Autowired
    protected TestRestTemplate restTemplate;

    // -> Actions ..................................................
    private ResponseEntity<CarDTO> getCar(String url) {
        return restTemplate.getForEntity(url, CarDTO.class);
    }

    private ResponseEntity<List<CarDTO>> getAllCars(String url) {

        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CarDTO>>() {
                    }
            );
        } catch (Exception e) {
            throw new DomainException("Necessita de um tipo válido");
        }
    }

    // -> Assertions ..................................................
    @Test
    public void ShouldGetListCars() {
        List<CarDTO> carros = getAllCars("/api/v1/cars").getBody();
        assertNotNull(carros);
        assertEquals(30, carros.size());
    }

    @Test
    public void ShouldGetListCarsByType() {
        assertEquals(10, getAllCars("/api/v1/cars/type/classicos").getBody().size());
        assertEquals(10, getAllCars("/api/v1/cars/type/esportivos").getBody().size());
        assertEquals(10, getAllCars("/api/v1/cars/type/luxo").getBody().size());

        assertEquals(HttpStatus.OK, getAllCars("/api/v1/cars/type/luxo").getStatusCode());
        try {
            getAllCars("/api/v1/cars/type/xxx");
            assertEquals(HttpStatus.NOT_FOUND, getAllCars("/api/v1/cars/type/xxx").getStatusCode());
        } catch (DomainException e) {
            assertEquals("Necessita de um tipo válido", e.getMessage());
        }
    }

    @Test
    public void shouldGetStatusOkAndNameCar() {
        ResponseEntity<CarDTO> response = getCar("/api/v1/cars/11");
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        CarDTO carDto = response.getBody();
        assertEquals("Ferrari FF", carDto.getName());
    }

    @Test
    public void shouldSaveCarAndGetStatusOk() {

        Car myCar = new Car(
                null,
                "Porsche 2",
                "Carro foda",
                "http://minha-url.com",
                "http://minha-url.com",
                "-123454",
                "-98432",
                "esportivo"
        );

        // Insert
        ResponseEntity response = restTemplate.postForEntity("/api/v1/cars", myCar, null);
        System.out.println(response);

        // testa se criou
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Buscar o objeto
        String location = response.getHeaders().get("location").get(0);
        CarDTO carDto = getCar(location).getBody();

        // testa nome e tipo
        assertNotNull(carDto);
        assertEquals("Porsche 2", carDto.getName());
        assertEquals("esportivo", carDto.getType());

        // Deletar o objeto
        restTemplate.delete(location);

        // testa se deletou
        assertEquals(HttpStatus.NOT_FOUND, getCar(location).getStatusCode());
    }

    @Test
    public void testGetNotFound() {
        ResponseEntity response = getCar("/api/v1/cars/1100");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
