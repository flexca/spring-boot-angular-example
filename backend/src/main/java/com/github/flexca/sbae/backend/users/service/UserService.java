package com.github.flexca.sbae.backend.users.service;

import com.github.flexca.sbae.backend.authentication.model.AccessScope;
import com.github.flexca.sbae.backend.authentication.service.SecurityService;
import com.github.flexca.sbae.backend.common.mapper.DateTimeMapper;
import com.github.flexca.sbae.backend.common.model.generic.Status;
import com.github.flexca.sbae.backend.common.model.generic.UpdateStatusRequest;
import com.github.flexca.sbae.backend.common.service.DateProvider;
import com.github.flexca.sbae.backend.common.utils.ValidationUtils;
import com.github.flexca.sbae.backend.email.service.MailService;
import com.github.flexca.sbae.backend.common.service.RandomGenerator;
import com.github.flexca.sbae.backend.common.utils.HashUtils;
import com.github.flexca.sbae.backend.errors.ErrorCode;
import com.github.flexca.sbae.backend.errors.ErrorType;
import com.github.flexca.sbae.backend.errors.SbaeException;
import com.github.flexca.sbae.backend.users.model.CompleteUserRegistrationRequest;
import com.github.flexca.sbae.backend.users.model.UserCreateRequest;
import com.github.flexca.sbae.backend.users.model.UserDto;
import com.github.flexca.sbae.backend.users.model.UserEntity;
import com.github.flexca.sbae.backend.users.model.UserResetPasswordRequest;
import com.github.flexca.sbae.backend.users.model.UserSearchRequest;
import com.github.flexca.sbae.backend.users.model.UserUpdateRequest;
import com.github.flexca.sbae.backend.users.model.UserValidateRegistrationTokenRequest;
import com.github.flexca.sbae.backend.users.model.UserValidateResetPasswordTokenRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import com.github.flexca.sbae.backend.common.model.search.SearchResponse;
import com.github.flexca.sbae.backend.users.mapper.UserMapper;
import com.github.flexca.sbae.backend.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final int MAX_SEARCH_REQUEST_LIMIT = 1000;

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final DateProvider dateProvider;
    private final RandomGenerator randomGenerator;
    private final SecurityService securityService;
    private final MailService mailService;

    @Value("${server.base-url}")
    private String baseUrl;

    public UserDto getById(String id) {
        UserEntity entity = userRepository.getById(id).orElseThrow(() ->
                new SbaeException("User with id [" + id + "] not found", ErrorType.ENTITY_NOT_FOUND, ErrorCode.USER_NOT_FOUND));
        return UserMapper.INSTANCE.toDto(entity);
    }

    public SearchResponse<UserDto> search(@ModelAttribute UserSearchRequest request) {

        if(request.getLimit() > MAX_SEARCH_REQUEST_LIMIT) {
            throw new SbaeException("Max limit exceed: " + MAX_SEARCH_REQUEST_LIMIT, ErrorType.INVALID_INPUT,
                    ErrorCode.SEARCH_EXCEED_MAX_LIMIT);
        }

        if(StringUtils.isNotBlank(request.getId()) && !ObjectId.isValid(request.getId())) {
            throw new SbaeException("Invalid id", ErrorType.INVALID_INPUT, ErrorCode.INVALID_ID);
        }

        SearchResponse<UserEntity> records = userRepository.search(request);
        List<UserDto> dtos = records.getItems().stream()
                .map(UserMapper.INSTANCE::toDto)
                .toList();
        return new SearchResponse<>(dtos, records.getLimit(), records.getOffset(), records.isNextPage());
    }

    public UserDto create(UserCreateRequest request) {

        securityService.validateHaveAccessToOrganization(request.getOrganizationId());

        userValidator.validateOrganization(request.getOrganizationId());
        userValidator.validateEmail(request.getEmail(), null);
        userValidator.validateFirstName(request.getFirstName());
        userValidator.validateLastName(request.getLastName());
        userValidator.validatePermissions(request.getPermissions());

        UserEntity entity = UserMapper.INSTANCE.toEntity(request);
        entity.setId(ObjectId.get());
        ZonedDateTime now = dateProvider.currentZonedDateTime();
        entity.setCreatedAt(DateTimeMapper.INSTANCE.toDate(now));
        entity.setStatus(Status.PENDING.getName());
        entity.setScope(AccessScope.ORGANIZATION.getName());
        String registrationToken = randomGenerator.nextRandomBytesHex(20);
        entity.setRegistrationToken(HashUtils.sha256(registrationToken));
        entity.setRegistrationTokenValidTo(DateTimeMapper.INSTANCE.toDate(now.plusDays(3)));
        entity.setOrganizationAdmin(false);

        userRepository.create(entity);

        String registrationLink = baseUrl + "/confirm-registration/" + registrationToken + "/user/" + entity.getId().toHexString();
        mailService.sendUserRegistrationEmail(entity.getEmail(), registrationLink);

        return UserMapper.INSTANCE.toDto(entity);
    }

    public UserDto update(String id, UserUpdateRequest request) {

        UserEntity entity = userRepository.getById(id).orElseThrow(() ->
                new SbaeException("User with id [" + id + "] not found", ErrorType.ENTITY_NOT_FOUND, ErrorCode.USER_NOT_FOUND));

        securityService.validateHaveAccessToOrganization(entity.getOrganizationId());

        userValidator.validateFirstName(request.getFirstName());
        userValidator.validateLastName(request.getLastName());

        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());

        if(!securityService.getCurrentUser().getId().equalsIgnoreCase(entity.getId().toHexString())) {
            userValidator.validatePermissions(request.getPermissions());
            entity.setPermissions(UserMapper.INSTANCE.toPermissions(request.getPermissions()));
        }

        userRepository.update(id, entity);

        return UserMapper.INSTANCE.toDto(entity);
    }

    public UserDto updateStatus(String id, UpdateStatusRequest request) {

        UserEntity entity = userRepository.getById(id).orElseThrow(() ->
                new SbaeException("User with id [" + id + "] not found", ErrorType.ENTITY_NOT_FOUND, ErrorCode.USER_NOT_FOUND));

        securityService.validateHaveAccessToOrganization(entity.getOrganizationId());

        if(securityService.getCurrentUser().getId().equalsIgnoreCase(entity.getId().toHexString())) {
            throw new SbaeException("You cannot change your own user status", ErrorType.INVALID_INPUT, ErrorCode.USER_SELF_STATUS_CHANGE_ERROR);
        }

        Status newStatus = Status.fromJsonString(request.getStatus());
        if(!Status.ACTIVE.equals(newStatus) && !Status.DISABLED.equals(newStatus)) {
            throw new SbaeException("Invalid status update", ErrorType.INVALID_INPUT, ErrorCode.ORGANIZATION_INVALID_STATUS_CHANGE);
        }

        entity.setStatus(newStatus.getName());

        userRepository.updateStatus(id, newStatus);

        return UserMapper.INSTANCE.toDto(entity);
    }

    public void validateRegistrationToken(String id, UserValidateRegistrationTokenRequest request) {

        UserEntity entity = userRepository.getById(id).orElseThrow(() ->
                new SbaeException("User with id [" + id + "] not found", ErrorType.ENTITY_NOT_FOUND, ErrorCode.USER_NOT_FOUND));

        userValidator.validateUserRegistrationToken(entity, request.getToken());
    }

    public void completeRegistration(String id, CompleteUserRegistrationRequest request) {

        UserEntity entity = userRepository.getById(id).orElseThrow(() ->
                new SbaeException("User with id [" + id + "] not found", ErrorType.ENTITY_NOT_FOUND, ErrorCode.USER_NOT_FOUND));

        userValidator.validateUserRegistrationToken(entity, request.getToken());
        userValidator.validatePassword(request.getUserPassword(), request.getUserPasswordConfirmation());

        userRepository.completeUserRegistration(id, HashUtils.sha256(request.getUserPassword()));
    }

    public void requestResetPassword(UserResetPasswordRequest request) {

        if(StringUtils.isBlank(request.getEmail())) {
            throw new SbaeException("Provide not empty email", ErrorType.INVALID_INPUT, ErrorCode.USER_EMPTY_EMAIL);
        }

        if(!ValidationUtils.isValidEmail(request.getEmail())) {
            throw new SbaeException("Provide valid email", ErrorType.INVALID_INPUT, ErrorCode.USER_INVALID_EMAIL);
        }

        UserEntity entity = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new SbaeException("User with email [" + request.getEmail() + "] not found", ErrorType.ENTITY_NOT_FOUND,
                        ErrorCode.USER_NOT_FOUND));

        if(!Status.ACTIVE.getName().equalsIgnoreCase(entity.getStatus())) {
            throw new SbaeException("Cannot reset password for not active user", ErrorType.INVALID_INPUT, ErrorCode.USER_NOT_ACTIVE);
        }

        ZonedDateTime now = dateProvider.currentZonedDateTime();
        String token = randomGenerator.nextRandomBytesHex(20);
        Date tokenValidTo = DateTimeMapper.INSTANCE.toDate(now.plusDays(3));

        userRepository.updateResetPasswordToken(entity.getId().toHexString(), HashUtils.sha256(token), tokenValidTo);

        String resetPasswordLink = baseUrl + "/reset-password/" + token + "/user/" + entity.getId().toHexString();
        mailService.sendResetPasswordEmail(entity.getEmail(), resetPasswordLink);
    }

    public void validateResetPasswordToken(String id, UserValidateResetPasswordTokenRequest request) {

        if(StringUtils.isBlank(request.getToken())) {
            throw new SbaeException("Provide not empty token", ErrorType.INVALID_INPUT, ErrorCode.USER_RESET_PASSWORD_TOKEN_INVALID);
        }

        UserEntity entity = userRepository.getById(id).orElseThrow(() ->
                new SbaeException("User with ID [" + id + "] not found", ErrorType.ENTITY_NOT_FOUND,
                        ErrorCode.USER_NOT_FOUND));

        userValidator.validateResetPasswordToken(entity, request.getToken());
    }

    public void completeResetPassword(String id, CompleteUserRegistrationRequest request) {

        UserEntity entity = userRepository.getById(id).orElseThrow(() ->
                new SbaeException("User with ID [" + id + "] not found", ErrorType.ENTITY_NOT_FOUND,
                        ErrorCode.USER_NOT_FOUND));

        userValidator.validatePassword(request.getUserPassword(), request.getUserPasswordConfirmation());
        userValidator.validateResetPasswordToken(entity, request.getToken());

        userRepository.completeResetPassword(id, HashUtils.sha256(request.getUserPassword()));
    }
}
