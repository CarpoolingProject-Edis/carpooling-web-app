package com.carpooling.main.service;


import com.carpooling.main.model.Car;
import com.carpooling.main.repository.interfaces.CarRepository;
import com.carpooling.main.service.interfaces.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> get() {
        return carRepository.get();
    }

    @Override
    public void create(Car car) {
        carRepository.create(car);
    }
}
