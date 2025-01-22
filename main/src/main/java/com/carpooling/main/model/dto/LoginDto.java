package com.carpooling.main.model.dto;


import jakarta.validation.constraints.*;

public class LoginDto {

    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 symbols")
    @NotEmpty(message = "Field cannot be empty!")
    private String username;
    @Size(min = 8, message = "Password must be at least 8 symbols and should contain " +
            "capital letter, digit and special symbol!")
    @NotEmpty(message = "Field cannot be empty!")
    private String password;

    public LoginDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
