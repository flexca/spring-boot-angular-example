package com.github.flexca.sbae.backend.users.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserFields {

    ID("_id"),
    ORGANIZATION_ID("organization_id"),
    ORGANIZATION_ADMIN("organization_admin"),
    EMAIL("email"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    STATUS("status"),
    SCOPE("scope"),
    CREATED_AT("created_at"),
    PASSWORD("password"),
    PERMISSIONS("permissions"),
    REGISTRATION_TOKEN("registration_token"),
    REGISTRATION_TOKEN_VALID_TO("registration_token_valid_to"),
    AUTHENTICATION_TOKEN("authentication_token"),
    REFRESH_TOKEN("refresh_token"),
    RESET_PASSWORD_TOKEN("reset_password_token"),
    RESET_PASSWORD_TOKEN_VALID_TO("reset_password_token_valid_to");

    @Getter
    private final String name;

}
