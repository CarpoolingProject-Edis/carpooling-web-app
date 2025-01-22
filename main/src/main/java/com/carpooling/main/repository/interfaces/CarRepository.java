package com.carpooling.main.repository.interfaces;


import com.carpooling.main.model.Car;
import java.util.List;

public interface CarRepository {

    List<Car> get();

    Car getById(int id);

    void create(Car car);

}
