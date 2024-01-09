package com.github.flexca.sbae.backend.authentication.service;

import com.github.flexca.sbae.backend.authentication.model.AccessScope;
import com.github.flexca.sbae.backend.authentication.model.SbaeAuthentication;
import com.github.flexca.sbae.backend.authentication.model.SbaePermission;
import com.github.flexca.sbae.backend.errors.ErrorCode;
import com.github.flexca.sbae.backend.errors.ErrorType;
import com.github.flexca.sbae.backend.errors.SbaeException;
import org.apache.commons.collections4.CollectionUtils;
import com.github.flexca.sbae.backend.users.model.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SecurityService {

    public SbaeAuthentication getAuthentication() {
        return (SbaeAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public UserDto getCurrentUser() {
        SbaeAuthentication authentication = getAuthentication();
        return (UserDto) authentication.getDetails();
    }

    public boolean hasPermission(String requiredPermission) {

        UserDto user = getCurrentUser();
        List<SbaePermission> permissions =  user.getPermissions();
        for(SbaePermission permission : permissions) {
            if(permission.getName().equalsIgnoreCase(requiredPermission)) {
                return true;
            }
        }

        throw new SbaeException("Missing required permission: " + requiredPermission, ErrorType.NOT_AUTHORIZED,
                ErrorCode.MISSING_REQUIRED_PERMISSION);
    }

    public boolean hasAnyOfPermission(String ... requiredPermissions) {

        Set<SbaePermission> requiredPermissionsSet = new HashSet<>();

        for(String requiredPermission : requiredPermissions) {
            requiredPermissionsSet.add(SbaePermission.fromJsonString(requiredPermission));
        }
        UserDto user = getCurrentUser();
        List<SbaePermission> permissions =  user.getPermissions();
        for(SbaePermission permission : permissions) {
            if(requiredPermissionsSet.contains(permission)) {
                return true;
            }
        }

        throw new SbaeException("Missing one of required permissions: " + requiredPermissionsSet, ErrorType.NOT_AUTHORIZED,
                ErrorCode.MISSING_REQUIRED_PERMISSION);
    }

    public void validateHaveAccessToOrganization(String organizationId) {
        UserDto user = getCurrentUser();
        if(AccessScope.GLOBAL.equals(user.getScope())) {
            return;
        }
        if(!user.getOrganizationId().equals(organizationId)) {
            throw new SbaeException("No access to organization", ErrorType.NOT_AUTHORIZED, ErrorCode.NO_ACCESS_TO_ORGANIZATION);
        }
    }

    public Set<SbaePermission> getCurrentUserPermissionsAsSet() {
        UserDto user = getCurrentUser();
        if(CollectionUtils.isEmpty(user.getPermissions())) {
            return Collections.emptySet();
        }
        Set<SbaePermission> permissions = new HashSet<>();
        for(SbaePermission permission : user.getPermissions()) {
            permissions.add(permission);
        }
        return permissions;
    }
}
