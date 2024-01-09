package com.github.flexca.sbae.backend.authentication.model;

import com.github.flexca.sbae.backend.common.model.generic.Status;
import lombok.Data;

import java.util.List;

@Data
public class UserClaim {

    private String id;
    private String organizationId;

    private String email;

    private String firstName;
    private String lastName;

    private AccessScope scope;

    private Status status;

    private List<SbaePermission> permissions;
}
