package com.github.flexca.sbae.backend.organizations.service;

import com.github.flexca.sbae.backend.common.mapper.DateTimeMapper;
import com.github.flexca.sbae.backend.common.model.generic.Status;
import com.github.flexca.sbae.backend.common.model.generic.UpdateStatusRequest;
import com.github.flexca.sbae.backend.common.service.DateProvider;
import com.github.flexca.sbae.backend.email.service.MailService;
import com.github.flexca.sbae.backend.common.service.RandomGenerator;
import com.github.flexca.sbae.backend.common.utils.HashUtils;
import com.github.flexca.sbae.backend.errors.ErrorCode;
import com.github.flexca.sbae.backend.errors.ErrorType;
import com.github.flexca.sbae.backend.errors.SbaeException;
import com.github.flexca.sbae.backend.organizations.mapper.OrganizationMapper;
import com.github.flexca.sbae.backend.organizations.model.OrganizationCompleteRegistrationRequest;
import com.github.flexca.sbae.backend.organizations.model.OrganizationDto;
import com.github.flexca.sbae.backend.organizations.model.OrganizationEntity;
import com.github.flexca.sbae.backend.organizations.model.OrganizationRegistrationRequest;
import com.github.flexca.sbae.backend.organizations.model.OrganizationUpdateRequest;
import com.github.flexca.sbae.backend.organizations.model.OrganizationValidateRegistrationTokenRequest;
import com.github.flexca.sbae.backend.organizations.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import com.github.flexca.sbae.backend.authentication.model.SbaePermission;
import com.github.flexca.sbae.backend.authentication.service.SecurityService;
import com.github.flexca.sbae.backend.users.mapper.UserMapper;
import com.github.flexca.sbae.backend.users.model.UserEntity;
import com.github.flexca.sbae.backend.authentication.model.AccessScope;
import com.github.flexca.sbae.backend.users.repository.UserRepository;
import com.github.flexca.sbae.backend.users.service.UserValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private static final int MAX_SEARCH_REQUEST_LIMIT = 1000;

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    private final OrganizationValidator organizationValidator;
    private final UserValidator userValidator;

    private final SecurityService securityService;

    private final RandomGenerator randomGenerator;
    private final DateProvider dateProvider;
    private final MailService mailService;

    @Value("${server.base-url}")
    private String baseUrl;

    public OrganizationDto requestRegistration(OrganizationRegistrationRequest request) {

        ZonedDateTime now = dateProvider.currentZonedDateTime();
        String registrationToken = randomGenerator.nextRandomBytesHex(20);

        organizationValidator.validateOrganizationName(request.getOrganizationName(), null);
        organizationValidator.validateOrganizationDescription(request.getOrganizationDescription());

        userValidator.validateEmail(request.getOrganizationAdminEmail(), null);
        userValidator.validateFirstName(request.getOrganizationAdminFirstName());
        userValidator.validateLastName(request.getOrganizationAdminLastName());

        OrganizationEntity entity = OrganizationMapper.INSTANCE.toEntity(request);
        entity.setId(ObjectId.get());
        entity.setStatus(Status.PENDING.getName());
        entity.setCreatedAt(DateTimeMapper.INSTANCE.toDate(now));
        entity.setRegistrationToken(HashUtils.sha256(registrationToken));
        entity.setRegistrationValidTo(DateTimeMapper.INSTANCE.toDate(now.plus(3, ChronoUnit.DAYS)));
        organizationRepository.create(entity);

        UserEntity userEntity = UserMapper.INSTANCE.toEntity(request);
        userEntity.setId(ObjectId.get());
        userEntity.setOrganizationId(entity.getId().toHexString());
        userEntity.setOrganizationAdmin(true);
        userEntity.setStatus(Status.PENDING.getName());
        userEntity.setScope(AccessScope.ORGANIZATION.getName());
        userEntity.setPermissions(SbaePermission.getAllNames());
        userEntity.setCreatedAt(DateTimeMapper.INSTANCE.toDate(now));
        userEntity.setOrganizationId(entity.getId().toHexString());
        userRepository.create(userEntity);

        String registrationLink = baseUrl + "/confirm-registration/" + registrationToken
                + "/organization/" + entity.getId().toHexString();

        mailService.sendOrganizationRegistrationEmail(userEntity.getEmail(), registrationLink);

        return OrganizationMapper.INSTANCE.toDto(entity);
    }

    public void validateRegistrationToken(String id, OrganizationValidateRegistrationTokenRequest request) {

        Optional<OrganizationEntity> organization = organizationRepository.getById(id);
        if(organization.isEmpty()) {
            throw new SbaeException("Organization not found", ErrorType.ENTITY_NOT_FOUND, ErrorCode.ORGANIZATION_NOT_FOUND);
        }

        OrganizationEntity entity = organization.get();

        organizationValidator.validateRegistrationToken(entity, request.getToken());
    }

    public void completeRegistration(String id, OrganizationCompleteRegistrationRequest request) {

        Optional<OrganizationEntity> organization = organizationRepository.getById(id);
        if(organization.isEmpty()) {
            throw new SbaeException("Organization not found", ErrorType.ENTITY_NOT_FOUND, ErrorCode.ORGANIZATION_NOT_FOUND);
        }

        OrganizationEntity entity = organization.get();

        organizationValidator.validateRegistrationToken(entity, request.getToken());

        userValidator.validatePassword(request.getAdminPassword(), request.getAdminPasswordConfirmation());

        organizationRepository.activateOrganization(id);
        userRepository.activateOrganizationAdmin(id, HashUtils.sha256(request.getAdminPassword()));
    }

    public OrganizationDto getById(String id) {

        securityService.validateHaveAccessToOrganization(id);

        OrganizationEntity entity = organizationRepository.getById(id).orElseThrow(() ->
                new SbaeException("Organization not found", ErrorType.ENTITY_NOT_FOUND, ErrorCode.ORGANIZATION_NOT_FOUND));

        return OrganizationMapper.INSTANCE.toDto(entity);
    }

    public OrganizationDto update(String id, OrganizationUpdateRequest request) {

        securityService.validateHaveAccessToOrganization(id);

        OrganizationEntity toUpdate = organizationRepository.getById(id).orElseThrow(() ->
                new SbaeException("Organization not found", ErrorType.ENTITY_NOT_FOUND, ErrorCode.ORGANIZATION_NOT_FOUND));

        organizationValidator.validateOrganizationStatus(Status.fromJsonString(toUpdate.getStatus()));
        organizationValidator.validateOrganizationName(request.getName(), toUpdate);
        organizationValidator.validateOrganizationDescription(request.getDescription());

        toUpdate.setName(request.getName());
        toUpdate.setDescription(request.getDescription());

        organizationRepository.update(id, toUpdate);

        return OrganizationMapper.INSTANCE.toDto(toUpdate);
    }

    public OrganizationDto updateStatus(String id, UpdateStatusRequest request) {

        securityService.validateHaveAccessToOrganization(id);

        OrganizationEntity toUpdate = organizationRepository.getById(id).orElseThrow(() ->
                new SbaeException("Organization not found", ErrorType.ENTITY_NOT_FOUND, ErrorCode.ORGANIZATION_NOT_FOUND));

        organizationValidator.validateOrganizationStatus(Status.fromJsonString(toUpdate.getStatus()));

        Status newStatus = Status.fromJsonString(request.getStatus());
        if(!Status.ACTIVE.equals(newStatus) && !Status.DISABLED.equals(newStatus)) {
            throw new SbaeException("Invalid status update", ErrorType.INVALID_INPUT, ErrorCode.ORGANIZATION_INVALID_STATUS_CHANGE);
        }

        toUpdate.setStatus(newStatus.getName());

        organizationRepository.updateStatus(id, newStatus);

        return OrganizationMapper.INSTANCE.toDto(toUpdate);
    }
}
