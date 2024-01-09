package com.github.flexca.sbae.backend.organizations.service;

import com.github.flexca.sbae.backend.common.mapper.DateTimeMapper;
import com.github.flexca.sbae.backend.common.model.generic.Status;
import com.github.flexca.sbae.backend.common.service.DateProvider;
import com.github.flexca.sbae.backend.common.utils.HashUtils;
import com.github.flexca.sbae.backend.errors.ErrorCode;
import com.github.flexca.sbae.backend.errors.ErrorType;
import com.github.flexca.sbae.backend.errors.SbaeException;
import com.github.flexca.sbae.backend.organizations.model.OrganizationEntity;
import com.github.flexca.sbae.backend.organizations.repository.OrganizationRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrganizationValidator {

    private static final int MAX_ORGANIZATION_NAME_LENGTH = 128;
    private static final int MAX_ORGANIZATION_DESCRIPTION_LENGTH = 255;

    private final OrganizationRepository organizationRepository;
    private final DateProvider dateProvider;

    public void validateOrganizationName(String name, OrganizationEntity existing) {

        if(StringUtils.isBlank(name)) {
            throw new SbaeException("Provide not blank organization name", ErrorType.INVALID_INPUT,
                    ErrorCode.ORGANIZATION_NAME_BLANK);
        }

        if(name.length() > MAX_ORGANIZATION_NAME_LENGTH) {
            throw new SbaeException("Organization name exceed max length: " + MAX_ORGANIZATION_NAME_LENGTH,
                    ErrorType.INVALID_INPUT, ErrorCode.ORGANIZATION_NAME_MAX_LENGTH_EXCEED);
        }

        Optional<OrganizationEntity> sameName = organizationRepository.findByName(name);
        if(sameName.isPresent() && (existing == null || !existing.getId().equals(sameName.get().getId()))) {
            throw new SbaeException("Organization name already used", ErrorType.INVALID_INPUT,
                    ErrorCode.ORGANIZATION_NAME_ALREADY_USED);
        }
    }

    public void validateOrganizationDescription(String description) {

        if(StringUtils.isBlank(description)) {
            return;
        }

        if(description.length() > MAX_ORGANIZATION_DESCRIPTION_LENGTH) {
            throw new SbaeException("Organization description exceed max length: " + MAX_ORGANIZATION_DESCRIPTION_LENGTH,
                    ErrorType.INVALID_INPUT, ErrorCode.ORGANIZATION_DESCRIPTION_MAX_LENGTH_EXCEED);
        }
    }

    public void validateRegistrationToken(OrganizationEntity entity, String requestToken) {

        if(!Status.PENDING.getName().equalsIgnoreCase(entity.getStatus())) {
            throw new SbaeException("Organization already confirmed", ErrorType.INVALID_INPUT, ErrorCode.ORGANIZATION_ALREADY_CONFIRMED);
        }

        ZonedDateTime now = dateProvider.currentZonedDateTime();
        ZonedDateTime expirationDate = DateTimeMapper.INSTANCE.toZonedDateTime(entity.getRegistrationValidTo());
        if(now.isAfter(expirationDate)) {
            throw new SbaeException("Organization confirmation expired", ErrorType.INVALID_INPUT, ErrorCode.ORGANIZATION_REGISTRATION_TOKEN_EXPIRED);
        }

        if(StringUtils.isBlank(requestToken)) {
            throw new SbaeException("Provide not blank token", ErrorType.INVALID_INPUT, ErrorCode.ORGANIZATION_REGISTRATION_TOKEN_EMPTY);
        }

        if(!HashUtils.sha256(requestToken).equalsIgnoreCase(entity.getRegistrationToken())) {
            throw new SbaeException("Invalid token", ErrorType.INVALID_INPUT, ErrorCode.ORGANIZATION_REGISTRATION_TOKEN_INVALID);
        }
    }

    public void validateOrganizationStatus(Status status) {
        if(Status.PENDING.equals(status)) {
            throw new SbaeException("Organization registration is not completed", ErrorType.INVALID_INPUT,
                    ErrorCode.ORGANIZATION_REGISTRATION_NOT_COMPLETED);
        }
    }
}
