package com.carpooling.main.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UpdateUserDto {
    @NotEmpty(message = "Field cannot be empty!")
    @Size(min = 2, max = 20, message = "First name must be between 2 and 20 symbols!")
    private String firstName;
    @NotEmpty(message = "Field cannot be empty!")
    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 symbols!")
    private String lastName;
    @NotEmpty(message = "Field cannot be empty!")
    private String email;
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 symbols")
    @NotEmpty(message = "Field cannot be empty!")
    private String username;
    @NotEmpty(message = "Field cannot be empty!")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits!")
    private String phoneNumber;


    public UpdateUserDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
