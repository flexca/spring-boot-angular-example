package com.github.flexca.sbae.backend.users.model;

import lombok.Data;

@Data
public class CompleteUserRegistrationRequest {

    private String token;
    private String userPassword;
    private String userPasswordConfirmation;
}
