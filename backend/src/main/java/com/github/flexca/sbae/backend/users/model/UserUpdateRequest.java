package com.github.flexca.sbae.backend.users.model;

import com.github.flexca.sbae.backend.authentication.model.SbaePermission;
import lombok.Data;

import java.util.List;

@Data
public class UserUpdateRequest {

    private String firstName;
    private String lastName;

    private List<SbaePermission> permissions;
}
