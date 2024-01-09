package com.github.flexca.sbae.backend.organizations.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrganizationFields {

    ID("_id"),
    NAME("name"),
    DESCRIPTION("description"),
    STATUS("status"),
    CREATED_AT("created_at"),
    REGISTRATION_TOKEN("registration_token"),
    REGISTRATION_VALID_TO("registration_valid_to");

    @Getter
    private final String name;
}
