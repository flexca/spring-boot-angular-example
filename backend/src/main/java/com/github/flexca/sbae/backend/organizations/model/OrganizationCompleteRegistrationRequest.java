package com.github.flexca.sbae.backend.organizations.model;

import lombok.Data;

@Data
public class OrganizationCompleteRegistrationRequest {

    private String token;
    private String adminPassword;
    private String adminPasswordConfirmation;
}
