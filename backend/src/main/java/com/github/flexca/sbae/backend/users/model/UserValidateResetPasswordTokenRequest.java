package com.github.flexca.sbae.backend.users.model;

import lombok.Data;

@Data
public class UserValidateResetPasswordTokenRequest {

    private String token;
}
