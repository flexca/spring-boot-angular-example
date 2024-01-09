package com.github.flexca.sbae.backend.users.model;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Data
public class UserEntity {

    private ObjectId id;

    private String organizationId;
    private boolean organizationAdmin;

    private String email;

    private String firstName;
    private String lastName;

    private String scope;

    private String status;

    private Date createdAt;

    private String password;

    private List<String> permissions;

    private String registrationToken;
    private Date registrationTokenValidTo;
    
    private String authenticationToken;
    private String refreshToken;

    private String resetPasswordToken;
    private Date resetPasswordTokenValidTo;
}
