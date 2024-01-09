package com.github.flexca.sbae.backend.users.service;

import com.github.flexca.sbae.backend.authentication.model.SbaePermission;
import com.github.flexca.sbae.backend.authentication.service.SecurityService;
import com.github.flexca.sbae.backend.common.mapper.DateTimeMapper;
import com.github.flexca.sbae.backend.common.model.generic.Status;
import com.github.flexca.sbae.backend.common.service.DateProvider;
import com.github.flexca.sbae.backend.common.utils.HashUtils;
import com.github.flexca.sbae.backend.common.utils.ValidationUtils;
import com.github.flexca.sbae.backend.errors.ErrorCode;
import com.github.flexca.sbae.backend.errors.ErrorType;
import com.github.flexca.sbae.backend.errors.SbaeException;
import com.github.flexca.sbae.backend.organizations.model.OrganizationEntity;
import com.github.flexca.sbae.backend.organizations.repository.OrganizationRepository;
import com.github.flexca.sbae.backend.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.github.flexca.sbae.backend.users.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private static final int EMAIL_MAX_LENGTH = 128;
    private static final int FIRST_NAME_MAX_LENGTH = 128;
    private static final int LAST_NAME_MAX_LENGTH = 128;

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 20;

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final DateProvider dateProvider;

    public void validateOrganization(String organizationId) {
        Optional<OrganizationEntity> organization = organizationRepository.getById(organizationId);
        if (organization.isEmpty()) {
            throw new SbaeException("Organization not found", ErrorType.ENTITY_NOT_FOUND, ErrorCode.ORGANIZATION_NOT_FOUND);
        }

        if (!Status.ACTIVE.getName().equalsIgnoreCase(organization.get().getStatus())) {
            throw new SbaeException("Organization not active", ErrorType.INVALID_INPUT, ErrorCode.ORGANIZATION_NOT_ACTIVE);
        }
    }

    public void validateEmail(String email, UserEntity existent) {

        if(StringUtils.isBlank(email)) {
            throw new SbaeException("Provide not empty email", ErrorType.INVALID_INPUT, ErrorCode.USER_EMPTY_EMAIL);
        }

        if(email.length() > EMAIL_MAX_LENGTH) {
            throw new SbaeException("Email exceed max length: " + EMAIL_MAX_LENGTH, ErrorType.INVALID_INPUT, ErrorCode.USER_EMAIL_EXCEED_MAX_LENGTH);
        }

        if(!ValidationUtils.isValidEmail(email)) {
            throw new SbaeException("Provide valid email", ErrorType.INVALID_INPUT, ErrorCode.USER_INVALID_EMAIL);
        }

        Optional<UserEntity> sameEmail = userRepository.findByEmail(email);
        if(sameEmail.isPresent() && (existent == null || !existent.getId().equals(sameEmail.get().getId()))) {
            throw new SbaeException("Email already used", ErrorType.INVALID_INPUT, ErrorCode.USER_EMAIL_ALREADY_USED);
        }
    }

    public void validateFirstName(String firstName) {

        if(StringUtils.isBlank(firstName)) {
            throw new SbaeException("Provide not empty first name", ErrorType.INVALID_INPUT, ErrorCode.USER_EMPTY_FIRST_NAME);
        }

        if(firstName.length() > FIRST_NAME_MAX_LENGTH) {
            throw new SbaeException("First name exceed max length: " + FIRST_NAME_MAX_LENGTH, ErrorType.INVALID_INPUT,
                    ErrorCode.USER_FIRST_NAME_EXCEED_MAX_LENGTH);
        }
    }

    public void validateLastName(String lastName) {

        if(StringUtils.isBlank(lastName)) {
            throw new SbaeException("Provide not empty last name", ErrorType.INVALID_INPUT, ErrorCode.USER_EMPTY_LAST_NAME);
        }

        if(lastName.length() > LAST_NAME_MAX_LENGTH) {
            throw new SbaeException("Last name exceed max length: " + LAST_NAME_MAX_LENGTH, ErrorType.INVALID_INPUT,
                    ErrorCode.USER_LAST_NAME_EXCEED_MAX_LENGTH);
        }
    }

    public void validatePermissions(List<SbaePermission> permissions) {

        if(CollectionUtils.isEmpty(permissions)) {
            throw new SbaeException("Assign to user at least one permission", ErrorType.INVALID_INPUT,
                    ErrorCode.USER_EMPTY_PERMISSIONS);
        }
        Set<SbaePermission> currentUserPermissions = securityService.getCurrentUserPermissionsAsSet();
        Set<SbaePermission> uniquePermissions = new HashSet<>();
        for(SbaePermission permission : permissions) {
            if(!currentUserPermissions.contains(permission)) {
                throw new SbaeException("You cannot assign permission you don't have: " + permission.getName(), ErrorType.INVALID_INPUT,
                        ErrorCode.USER_PERMISSION_CANNOT_BE_ASSIGNED);
            }
            if(!uniquePermissions.add(permission)) {
                throw new SbaeException("Duplicated permission: " + permission.getName(), ErrorType.INVALID_INPUT,
                        ErrorCode.USER_PERMISSION_DUPLICATE);
            }
        }
    }

    public void validatePassword(String password, String passwordConfirmation) {

        if(StringUtils.isBlank(password)) {
            throw new SbaeException("Provide not blank password", ErrorType.INVALID_INPUT, ErrorCode.USER_EMPTY_PASSWORD);
        }

        if(!password.equals(passwordConfirmation)) {
            throw new SbaeException("Password and confirmation of password not equals", ErrorType.INVALID_INPUT,
                    ErrorCode.USER_PASSWORD_CONFORMATION_DIFFERENT);
        }

        if(password.length() < MIN_PASSWORD_LENGTH) {
            throw new SbaeException("User password min length: " + MIN_PASSWORD_LENGTH, ErrorType.INVALID_INPUT,
                    ErrorCode.USER_PASSWORD_TOO_SHORT);
        }

        if(password.length() > MAX_PASSWORD_LENGTH) {
            throw new SbaeException("User password exceed max length: " + MAX_PASSWORD_LENGTH, ErrorType.INVALID_INPUT,
                    ErrorCode.USER_PASSWORD_EXCEED_MAX_LENGTH);
        }
    }

    public void validateUserRegistrationToken(UserEntity entity, String token) {

        if(!Status.PENDING.getName().equalsIgnoreCase(entity.getStatus())) {
            throw new SbaeException("User already confirmed", ErrorType.INVALID_INPUT, ErrorCode.USER_ALREADY_CONFIRMED);
        }

        if(StringUtils.isBlank(token)) {
            throw new SbaeException("User blank registration token", ErrorType.INVALID_INPUT, ErrorCode.USER_REGISTRATION_TOKEN_INVALID);
        }

        if(!HashUtils.sha256(token).equalsIgnoreCase(entity.getRegistrationToken())) {
            throw new SbaeException("User invalid registration token", ErrorType.INVALID_INPUT, ErrorCode.USER_REGISTRATION_TOKEN_INVALID);
        }

        ZonedDateTime now = dateProvider.currentZonedDateTime();
        ZonedDateTime expiration = DateTimeMapper.INSTANCE.toZonedDateTime(entity.getRegistrationTokenValidTo());
        if(now.isAfter(expiration)) {
            throw new SbaeException("User expired registration token", ErrorType.INVALID_INPUT, ErrorCode.USER_REGISTRATION_TOKEN_EXPIRED);
        }
    }

    public void validateResetPasswordToken(UserEntity entity, String token) {

        if(!Status.ACTIVE.getName().equalsIgnoreCase(entity.getStatus())) {
            throw new SbaeException("Cannot reset password for not active user", ErrorType.INVALID_INPUT, ErrorCode.USER_NOT_ACTIVE);
        }

        if(StringUtils.isBlank(token)) {
            throw new SbaeException("User blank reset password token", ErrorType.INVALID_INPUT, ErrorCode.USER_REGISTRATION_TOKEN_INVALID);
        }

        if(!HashUtils.sha256(token).equalsIgnoreCase(entity.getResetPasswordToken())) {
            throw new SbaeException("User invalid reset password token", ErrorType.INVALID_INPUT, ErrorCode.USER_RESET_PASSWORD_TOKEN_INVALID);
        }

        ZonedDateTime now = dateProvider.currentZonedDateTime();
        ZonedDateTime expiration = DateTimeMapper.INSTANCE.toZonedDateTime(entity.getResetPasswordTokenValidTo());
        if(now.isAfter(expiration)) {
            throw new SbaeException("User expired registration token", ErrorType.INVALID_INPUT, ErrorCode.USER_RESET_PASSWORD_TOKEN_EXPIRED);
        }
    }
}
