package com.github.flexca.sbae.backend.users.mapper;

import com.github.flexca.sbae.backend.authentication.mapper.AuthenticationMapper;
import com.github.flexca.sbae.backend.authentication.model.AccessScope;
import com.github.flexca.sbae.backend.authentication.model.SbaePermission;
import com.github.flexca.sbae.backend.common.mapper.DateTimeMapper;
import com.github.flexca.sbae.backend.common.model.generic.Status;
import com.github.flexca.sbae.backend.organizations.model.OrganizationRegistrationRequest;
import com.github.flexca.sbae.backend.users.model.UserCreateRequest;
import com.github.flexca.sbae.backend.users.model.UserDto;
import com.github.flexca.sbae.backend.users.model.UserEntity;
import com.github.flexca.sbae.backend.users.model.UserFields;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    default UserEntity toEntity(UserDto dto) {
        UserEntity entity = new UserEntity();
        entity.setId(new ObjectId(dto.getId()));
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setStatus(dto.getStatus().getName());
        entity.setScope(dto.getScope().getName());
        entity.setCreatedAt(DateTimeMapper.INSTANCE.toDate(dto.getCreatedAt()));
        return entity;
    }

    default UserEntity toEntity(UserCreateRequest request) {
        UserEntity entity = new UserEntity();
        entity.setOrganizationId(request.getOrganizationId());
        entity.setEmail(request.getEmail());
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setPermissions(UserMapper.INSTANCE.toPermissions(request.getPermissions()));
        return entity;
    }

    default UserEntity toEntity(Document document) {

        UserEntity entity = new UserEntity();

        entity.setId(document.getObjectId(UserFields.ID.getName()));
        entity.setOrganizationId(document.getString(UserFields.ORGANIZATION_ID.getName()));
        entity.setOrganizationAdmin(Boolean.TRUE.equals(document.getBoolean(UserFields.ORGANIZATION_ADMIN.getName())));
        entity.setEmail(document.getString(UserFields.EMAIL.getName()));
        entity.setFirstName(document.getString(UserFields.FIRST_NAME.getName()));
        entity.setLastName(document.getString(UserFields.LAST_NAME.getName()));
        entity.setStatus(document.getString(UserFields.STATUS.getName()));
        entity.setScope(document.getString(UserFields.SCOPE.getName()));
        entity.setCreatedAt(document.getDate(UserFields.CREATED_AT.getName()));
        entity.setPermissions(document.getList(UserFields.PERMISSIONS.getName(), String.class));

        entity.setPassword(document.getString(UserFields.PASSWORD.getName()));
        entity.setRegistrationToken(document.getString(UserFields.REGISTRATION_TOKEN.getName()));
        entity.setRegistrationTokenValidTo(document.getDate(UserFields.REGISTRATION_TOKEN_VALID_TO.getName()));
        entity.setAuthenticationToken(document.getString(UserFields.AUTHENTICATION_TOKEN.getName()));
        entity.setRefreshToken(document.getString(UserFields.REFRESH_TOKEN.getName()));
        entity.setResetPasswordToken(document.getString(UserFields.RESET_PASSWORD_TOKEN.getName()));
        entity.setResetPasswordTokenValidTo(document.getDate(UserFields.RESET_PASSWORD_TOKEN_VALID_TO.getName()));

        return entity;
    }

    default UserDto toDto(UserEntity entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId().toHexString());
        dto.setEmail(entity.getEmail());
        dto.setOrganizationId(entity.getOrganizationId());
        dto.setOrganizationAdmin(entity.isOrganizationAdmin());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setStatus(Status.fromJsonString(entity.getStatus()));
        dto.setScope(AccessScope.fromJsonString(entity.getScope()));
        dto.setCreatedAt(DateTimeMapper.INSTANCE.toZonedDateTime(entity.getCreatedAt()));
        dto.setPermissions(AuthenticationMapper.INSTANCE.toPermissions(entity.getPermissions()));
        return dto;
    }

    default UserEntity toEntity(OrganizationRegistrationRequest request) {
        UserEntity entity = new UserEntity();
        entity.setEmail(request.getOrganizationAdminEmail());
        entity.setFirstName(request.getOrganizationAdminFirstName());
        entity.setLastName(request.getOrganizationAdminLastName());
        return entity;
    }

    default Document toDocument(UserEntity user) {
        Document document = new Document();
        return document.append(UserFields.ID.getName(), user.getId())
                .append(UserFields.ORGANIZATION_ID.getName(), user.getOrganizationId())
                .append(UserFields.ORGANIZATION_ADMIN.getName(), user.isOrganizationAdmin())
                .append(UserFields.EMAIL.getName(), user.getEmail())
                .append(UserFields.FIRST_NAME.getName(), user.getFirstName())
                .append(UserFields.LAST_NAME.getName(), user.getLastName())
                .append(UserFields.STATUS.getName(), user.getStatus())
                .append(UserFields.SCOPE.getName(), user.getScope())
                .append(UserFields.CREATED_AT.getName(), user.getCreatedAt())
                .append(UserFields.PERMISSIONS.getName(), user.getPermissions())
                .append(UserFields.PASSWORD.getName(), user.getPassword())
                .append(UserFields.REGISTRATION_TOKEN.getName(), user.getRegistrationToken())
                .append(UserFields.REGISTRATION_TOKEN_VALID_TO.getName(), user.getRegistrationTokenValidTo());
    }

    default List<String> toPermissions(List<SbaePermission> permissions) {

        if(CollectionUtils.isEmpty(permissions)) {
            return Collections.emptyList();
        }

        return permissions.stream().map(permission -> permission.getName()).toList();
    }
}
