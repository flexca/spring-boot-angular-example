package com.github.flexca.sbae.backend.organizations.model;

import lombok.Data;

@Data
public class OrganizationRegistrationRequest {

    private String organizationName;
    private String organizationDescription;

    private String organizationAdminFirstName;
    private String organizationAdminLastName;
    private String organizationAdminEmail;
}
