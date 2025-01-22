package com.carpooling.main.service.interfaces;


import com.carpooling.main.model.Car;

import java.util.List;

public interface CarService {
    List<Car> get();

    void create(Car car);
}
