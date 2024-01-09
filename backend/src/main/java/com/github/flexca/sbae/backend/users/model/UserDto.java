package com.github.flexca.sbae.backend.users.model;

import com.github.flexca.sbae.backend.authentication.model.AccessScope;
import com.github.flexca.sbae.backend.authentication.model.SbaePermission;
import com.github.flexca.sbae.backend.common.model.generic.Status;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class UserDto {

    private String id;

    private String organizationId;
    private boolean organizationAdmin;

    private String email;

    private String firstName;
    private String lastName;

    private AccessScope scope;

    private Status status;

    private ZonedDateTime createdAt;

    private List<SbaePermission> permissions;

}
