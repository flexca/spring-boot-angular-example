package com.github.flexca.sbae.backend.organizations.model;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;

@Data
public class OrganizationEntity {

    private ObjectId id;
    private String name;
    private String description;
    private String status;
    private Date createdAt;
    private String registrationToken;
    private Date registrationValidTo;
}
