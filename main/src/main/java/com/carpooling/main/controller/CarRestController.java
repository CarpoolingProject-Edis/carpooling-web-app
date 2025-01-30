package com.carpooling.main.controller;

import com.carpooling.main.exceptions.EntityDuplicateException;
import com.carpooling.main.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.carpooling.main.service.interfaces.CarService;
import com.carpooling.main.helpers.mapper.CarMapper;

import java.util.List;


@RestController
@RequestMapping("/api/cars")
public class CarRestController {
    private final CarService carService;
    private final CarMapper carMapper;

    @Autowired
    public CarRestController(CarService carService, CarMapper carMapper) {
        this.carService = carService;
        this.carMapper = carMapper;
    }

    @GetMapping
    public List<Car> get() {
        return carService.get();
    }


    @PostMapping
    public Car createCar(@RequestBody @Validated Car car) {
        try {
            carService.create(car);
            return car;
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
