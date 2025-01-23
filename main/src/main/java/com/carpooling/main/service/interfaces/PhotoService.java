package com.carpooling.main.service.interfaces;



import com.carpooling.main.model.Photo;

import java.io.IOException;

public interface PhotoService {
    String uploadPhoto(byte[] imageDate) throws IOException;

    void create(Photo photo);
}
