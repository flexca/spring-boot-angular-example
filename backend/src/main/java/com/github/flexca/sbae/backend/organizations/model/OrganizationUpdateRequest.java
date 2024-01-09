package com.github.flexca.sbae.backend.organizations.model;

import lombok.Data;

@Data
public class OrganizationUpdateRequest {

    private String name;
    private String description;
}
