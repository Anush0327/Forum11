package com.prodapt.learningspring.controller.binding;

import lombok.Data;

@Data
public class RegistrationForm {
    private String name;
    private String email;
    private String dateOfBirth;
    private String password;
    private String repeatPassword;
    

    public boolean isValid() {
        return password.equals(repeatPassword);
    }
}
