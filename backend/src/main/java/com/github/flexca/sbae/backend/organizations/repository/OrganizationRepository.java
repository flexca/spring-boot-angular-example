package com.github.flexca.sbae.backend.organizations.repository;

import com.github.flexca.sbae.backend.common.model.generic.Status;
import com.github.flexca.sbae.backend.organizations.mapper.OrganizationMapper;
import com.github.flexca.sbae.backend.organizations.model.OrganizationEntity;
import com.github.flexca.sbae.backend.organizations.model.OrganizationFields;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.github.flexca.sbae.backend.users.model.UserFields;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrganizationRepository {

    private static final String COLLECTION_NAME = "organizations";

    private final MongoDatabase mongoDatabase;

    public void create(OrganizationEntity entity) {

        MongoCollection<Document> organizationCollection = mongoDatabase.getCollection(COLLECTION_NAME);
        organizationCollection.insertOne(OrganizationMapper.INSTANCE.toDocument(entity));
    }

    public Optional<OrganizationEntity> findByName(String name) {

        MongoCollection<Document> organizationCollection = mongoDatabase.getCollection(COLLECTION_NAME);
        Document document = organizationCollection.find(Filters.eq(OrganizationFields.NAME.getName(), name)).first();
        if(document == null) {
            return Optional.empty();
        }
        return Optional.of(OrganizationMapper.INSTANCE.toEntity(document));
    }

    public Optional<OrganizationEntity> getById(String id) {
        MongoCollection<Document> organizationCollection = mongoDatabase.getCollection(COLLECTION_NAME);
        Document document = organizationCollection.find(Filters.eq(UserFields.ID.getName(), new ObjectId(id))).first();
        if(document == null) {
            return Optional.empty();
        }
        return Optional.of(OrganizationMapper.INSTANCE.toEntity(document));
    }

    public void activateOrganization(String id) {

        MongoCollection<Document> organizationCollection = mongoDatabase.getCollection(COLLECTION_NAME);
        Bson filter = Filters.eq(UserFields.ID.getName(), new ObjectId(id));
        Bson updates = Updates.combine(
                Updates.set(OrganizationFields.STATUS.getName(), Status.ACTIVE.getName()),
                Updates.set(OrganizationFields.REGISTRATION_TOKEN.getName(), null),
                Updates.set(OrganizationFields.REGISTRATION_VALID_TO.getName(), null));
        organizationCollection.updateOne(filter, updates);
    }

    public void update(String id, OrganizationEntity toUpdate) {

        MongoCollection<Document> organizationCollection = mongoDatabase.getCollection(COLLECTION_NAME);
        Bson filter = Filters.eq(UserFields.ID.getName(), new ObjectId(id));
        Bson updates = Updates.combine(
                Updates.set(OrganizationFields.NAME.getName(), toUpdate.getName()),
                Updates.set(OrganizationFields.DESCRIPTION.getName(), toUpdate.getDescription()));
        organizationCollection.updateOne(filter, updates);
    }

    public void updateStatus(String id, Status newStatus) {
        MongoCollection<Document> organizationCollection = mongoDatabase.getCollection(COLLECTION_NAME);
        Bson filter = Filters.eq(UserFields.ID.getName(), new ObjectId(id));
        Bson updates = Updates.combine(
                Updates.set(OrganizationFields.STATUS.getName(), newStatus.getName()));
        organizationCollection.updateOne(filter, updates);
    }
}
