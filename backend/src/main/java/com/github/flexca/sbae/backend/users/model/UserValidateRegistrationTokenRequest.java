package com.github.flexca.sbae.backend.users.model;

import lombok.Data;

@Data
public class UserValidateRegistrationTokenRequest {

    private String token;
}
