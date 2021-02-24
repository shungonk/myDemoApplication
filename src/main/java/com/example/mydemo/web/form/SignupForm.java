package com.example.mydemo.web.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SignupForm {
    @Size(min=4, max=30, message="Size must be between 4 and 30")
    @Pattern(regexp="^\\w*$", message="Each character must be alphanumeric or underscore (A-Za-z0-9_)")
    private String username;

    @Size(min=8, max=100, message="Size must be between 8 and 100")
    private String password;

    public SignupForm() {}

    public SignupForm(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}