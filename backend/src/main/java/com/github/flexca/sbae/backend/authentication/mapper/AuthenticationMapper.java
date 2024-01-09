package com.github.flexca.sbae.backend.authentication.mapper;

import com.github.flexca.sbae.backend.authentication.model.SbaePermission;
import com.github.flexca.sbae.backend.authentication.model.UserClaim;
import org.apache.commons.collections4.CollectionUtils;
import com.github.flexca.sbae.backend.users.model.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper
public interface AuthenticationMapper {

    AuthenticationMapper INSTANCE = Mappers.getMapper(AuthenticationMapper.class);

    default List<SbaePermission> toPermissions(List<String> input) {

        if (CollectionUtils.isEmpty(input)) {
            return Collections.emptyList();
        }

        List<SbaePermission> permissions = new ArrayList<>();
        for (String permissionString : input) {
            SbaePermission permission = SbaePermission.fromJsonString(permissionString);
            if (permission != null) {
                permissions.add(permission);
            }
        }
        return permissions;
    }

    default UserClaim toClaim(UserDto user) {

        UserClaim claim = new UserClaim();
        claim.setId(user.getId());
        claim.setOrganizationId(user.getOrganizationId());
        claim.setStatus(user.getStatus());
        claim.setScope(user.getScope());
        claim.setPermissions(user.getPermissions());
        claim.setEmail(user.getEmail());
        claim.setFirstName(user.getFirstName());
        claim.setLastName(user.getLastName());
        return claim;
    }
}
