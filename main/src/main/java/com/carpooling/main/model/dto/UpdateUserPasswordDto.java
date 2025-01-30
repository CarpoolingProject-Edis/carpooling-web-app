package com.carpooling.main.model.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UpdateUserPasswordDto {
    @NotEmpty(message = "Field cannot be empty!")
    private String password;

    @NotEmpty(message = "Field cannot be empty!")
    @Size(min = 8, message = "Password must be at least 8 symbols and should contain " +
            "capital letter, digit and special symbol!")
    private String changePassword;

    @NotEmpty(message = "Field cannot be empty!")
    @Size(min = 8, message = "Password must be at least 8 symbols and should contain " +
            "capital letter, digit and special symbol!")
    private String changePasswordConfirm;

    public UpdateUserPasswordDto() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChangePasswordConfirm() {
        return changePasswordConfirm;
    }

    public void setChangePasswordConfirm(String changePasswordConfirm) {
        this.changePasswordConfirm = changePasswordConfirm;
    }

    public String getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(String changePassword) {
        this.changePassword = changePassword;
    }
}
