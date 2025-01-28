//package com.carpooling.main.service;
//
//import com.carpooling.main.model.Photo;
//import com.carpooling.main.repository.interfaces.PhotoUrlRepository;
//import com.carpooling.main.service.interfaces.PhotoService;
//import com.cloudinary.Cloudinary;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Service
//public class PhotoServiceImpl implements PhotoService {
//    private final Cloudinary cloudinary;
//    private final PhotoUrlRepository photoUrlRepository;
//
//    @Autowired
//    public PhotoServiceImpl(Cloudinary cloudinary, PhotoUrlRepository photoUrlRepository) {
//        this.cloudinary = cloudinary;
//        this.photoUrlRepository = photoUrlRepository;
//    }
//
//    @Override
//    public String uploadPhoto(byte[] imageDate) throws IOException {
//        Map uploadResult = cloudinary.uploader().upload(imageDate, null);
//
//        return (String) uploadResult.get("secure_url");
//    }
//
//    @Override
//    public void create(Photo photo) {
//        photoUrlRepository.create(photo);
//    }
//}
