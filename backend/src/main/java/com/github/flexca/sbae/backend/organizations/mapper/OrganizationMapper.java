package com.github.flexca.sbae.backend.organizations.mapper;

import com.github.flexca.sbae.backend.common.mapper.DateTimeMapper;
import com.github.flexca.sbae.backend.common.model.generic.Status;
import com.github.flexca.sbae.backend.organizations.model.OrganizationDto;
import com.github.flexca.sbae.backend.organizations.model.OrganizationRegistrationRequest;
import org.bson.Document;
import com.github.flexca.sbae.backend.organizations.model.OrganizationEntity;
import com.github.flexca.sbae.backend.organizations.model.OrganizationFields;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrganizationMapper {

    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

    default OrganizationEntity toEntity(OrganizationRegistrationRequest request) {
        OrganizationEntity entity = new OrganizationEntity();
        entity.setName(request.getOrganizationName());
        entity.setDescription(request.getOrganizationDescription());
        return entity;
    }

    default Document toDocument(OrganizationEntity entity) {
        return new Document()
                .append(OrganizationFields.ID.getName(), entity.getId())
                .append(OrganizationFields.NAME.getName(), entity.getName())
                .append(OrganizationFields.DESCRIPTION.getName(), entity.getDescription())
                .append(OrganizationFields.STATUS.getName(), entity.getStatus())
                .append(OrganizationFields.CREATED_AT.getName(), entity.getCreatedAt())
                .append(OrganizationFields.REGISTRATION_TOKEN.getName(), entity.getRegistrationToken())
                .append(OrganizationFields.REGISTRATION_VALID_TO.getName(), entity.getRegistrationValidTo());
    }

    default OrganizationEntity toEntity(Document document) {
        OrganizationEntity entity = new OrganizationEntity();
        entity.setId(document.getObjectId(OrganizationFields.ID.getName()));
        entity.setName(document.getString(OrganizationFields.NAME.getName()));
        entity.setDescription(document.getString(OrganizationFields.DESCRIPTION.getName()));
        entity.setStatus(document.getString(OrganizationFields.STATUS.getName()));
        entity.setCreatedAt(document.getDate(OrganizationFields.CREATED_AT.getName()));
        entity.setRegistrationToken(document.getString(OrganizationFields.REGISTRATION_TOKEN.getName()));
        entity.setRegistrationValidTo(document.getDate(OrganizationFields.REGISTRATION_VALID_TO.getName()));
        return entity;
    }

    default OrganizationDto toDto(OrganizationEntity entity) {
        OrganizationDto dto = new OrganizationDto();
        dto.setId(entity.getId().toHexString());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(Status.fromJsonString(entity.getStatus()));
        dto.setCreatedAt(DateTimeMapper.INSTANCE.toZonedDateTime(entity.getCreatedAt()));
        return dto;
    }
}
