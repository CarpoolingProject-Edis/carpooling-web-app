package com.carpooling.main.repository.interfaces;

import com.carpooling.main.model.Photo;

public interface PhotoUrlRepository {
    Photo getById(int id);

    void create(Photo photo);
}
