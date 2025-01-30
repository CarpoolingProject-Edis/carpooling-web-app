package com.carpooling.main.helpers.mapper;


import com.carpooling.main.model.User;
import com.carpooling.main.model.dto.RegisterDto;
import com.carpooling.main.model.dto.UpdateUserDto;
import com.carpooling.main.model.dto.UpdateUserPasswordDto;
import com.carpooling.main.model.enums.UserRole;
import com.carpooling.main.model.enums.UserStatus;
import com.carpooling.main.repository.interfaces.CarRepository;
import com.carpooling.main.repository.interfaces.PhotoUrlRepository;
import com.carpooling.main.repository.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final UserRepository userRepository;
    private final PhotoUrlRepository photoUrlRepository;
    private final CarRepository carRepository;

    @Autowired
    public UserMapper(UserRepository userRepository, PhotoUrlRepository photoUrlRepository, CarRepository carRepository) {
        this.userRepository = userRepository;
        this.photoUrlRepository = photoUrlRepository;
        this.carRepository = carRepository;
    }

    public User fromDto(RegisterDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());

        user.setCar(carRepository.getById(1));
        user.setPhotoUrl(photoUrlRepository.getById(1));
        user.setRating(0.0);
        user.setUserRole(UserRole.USER);
        user.setUserStatus(UserStatus.ACTIVE);
        return user;
    }

    public User fromDto(int id, UpdateUserDto dto) {
        User user = userRepository.getById(id);
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        return user;
    }

    public User fromDto(int id, UpdateUserPasswordDto dto) {
        User user = userRepository.getById(id);
        user.setPassword(dto.getChangePassword());
        return user;
    }
    public UpdateUserPasswordDto toDto(User user) {
        UpdateUserPasswordDto dto = new UpdateUserPasswordDto();
        dto.setPassword(user.getPassword());
        return dto;
    }

}
