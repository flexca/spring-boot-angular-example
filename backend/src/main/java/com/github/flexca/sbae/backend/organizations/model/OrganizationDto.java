package com.github.flexca.sbae.backend.organizations.model;

import com.github.flexca.sbae.backend.common.model.generic.Status;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class OrganizationDto {

    private String id;
    private String name;
    private String description;
    private Status status;
    private ZonedDateTime createdAt;
}
