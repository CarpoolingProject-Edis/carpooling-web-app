package com.carpooling.main.model.dto;


import jakarta.validation.constraints.*;

public class RegisterDto extends LoginDto {

    @NotEmpty(message = "Field cannot be empty!")
    @Size(min = 2, max = 20, message = "First name must be between 2 and 20 symbols!")
    private String firstName;
    @NotEmpty(message = "Field cannot be empty!")
    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 symbols!")
    private String lastName;
    @NotEmpty(message = "Field cannot be empty!")
    private String email;
    @NotEmpty(message = "Field cannot be empty!")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits!")
    private String phoneNumber;
    @NotEmpty(message = "Field cannot be empty!")
    private String confirmPassword;

    public RegisterDto() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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


}
