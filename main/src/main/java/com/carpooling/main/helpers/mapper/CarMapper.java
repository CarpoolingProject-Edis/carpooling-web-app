package com.carpooling.main.helpers.mapper;


import com.carpooling.main.model.Car;
import com.carpooling.main.repository.interfaces.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.carpooling.main.model.dto.CarDto;

@Component
public class CarMapper {
    private final CarRepository carRepository;

    @Autowired
    public CarMapper(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car fromDto(CarDto dto) {
        Car car = new Car();
        car.setModel(dto.getModel());
        car.setYear(dto.getYear());
        car.setType(dto.getType());
        return car;
    }
}
