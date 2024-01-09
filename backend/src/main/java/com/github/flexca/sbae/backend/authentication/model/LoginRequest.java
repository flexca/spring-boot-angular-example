package com.github.flexca.sbae.backend.authentication.model;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
}
