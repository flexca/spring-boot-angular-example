package com.github.flexca.sbae.backend.users.repository;

import com.github.flexca.sbae.backend.common.mapper.DateTimeMapper;
import com.github.flexca.sbae.backend.common.model.generic.Status;
import com.github.flexca.sbae.backend.users.mapper.UserMapper;
import com.github.flexca.sbae.backend.users.model.UserEntity;
import com.github.flexca.sbae.backend.users.model.UserFields;
import com.github.flexca.sbae.backend.users.model.UserSearchRequest;
import com.mongodb.client.*;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.github.flexca.sbae.backend.common.model.search.SearchResponse;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.mongodb.client.model.Filters;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private static final String COLLECTION_NAME = "users";

    private final MongoDatabase mongoDatabase;

    @PostConstruct
    public void init() {
        MongoCollection<Document> userCollection = mongoDatabase.getCollection(COLLECTION_NAME);
        ListIndexesIterable<Document> indexes = userCollection.listIndexes();
        //for(Document index : indexes) {
        //    index.
        //}
    }

    public void create(UserEntity user) {

        MongoCollection<Document> userCollection = mongoDatabase.getCollection(COLLECTION_NAME);
        userCollection.insertOne(UserMapper.INSTANCE.toDocument(user));

    }

    public Optional<UserEntity> getById(String id) {

        MongoCollection<Document> userCollection = mongoDatabase.getCollection(COLLECTION_NAME);
        Document document = userCollection.find(Filters.eq(UserFields.ID.getName(), new ObjectId(id))).first();
        if(document == null) {
            return Optional.empty();
        }
        return Optional.of(UserMapper.INSTANCE.toEntity(document));
    }

    public Optional<UserEntity> findByEmail(String email) {
        MongoCollection<Document> userCollection = mongoDatabase.getCollection(COLLECTION_NAME);
        Document document = userCollection.find(Filters.eq(UserFields.EMAIL.getName(), email)).first();
        if(document == null) {
            return Optional.empty();
        }
        return Optional.of(UserMapper.INSTANCE.toEntity(document));
    }

    public void setUserTokens(ObjectId id, String token, String refreshToken) {

    }

    public SearchResponse<UserEntity> search(UserSearchRequest request) {

        Bson filters = Filters.empty();
        if(StringUtils.isNotBlank(request.getOrganizationId())) {
            filters = Filters.and(filters, Filters.eq(UserFields.ORGANIZATION_ID.getName(), request.getOrganizationId()));
        }
        if(StringUtils.isNotBlank(request.getId()) && ObjectId.isValid(request.getId())) {
            filters = Filters.and(filters, Filters.eq(UserFields.ID.getName(), new ObjectId(request.getId())));
        }
        if(StringUtils.isNotBlank(request.getEmail())) {
            Pattern pattern = Pattern.compile("^"+Pattern.quote(request.getEmail()), Pattern.CASE_INSENSITIVE);
            filters = Filters.and(filters, Filters.regex(UserFields.EMAIL.getName(), pattern));
        }
        if(StringUtils.isNotBlank(request.getName())) {
            Pattern pattern = Pattern.compile("^"+Pattern.quote(request.getName()), Pattern.CASE_INSENSITIVE);
            Bson nameFilters = Filters.regex(UserFields.FIRST_NAME.getName(), pattern);
            nameFilters = Filters.or(nameFilters, Filters.regex(UserFields.LAST_NAME.getName(), pattern));
            filters = Filters.and(filters, nameFilters);
        }
        if(StringUtils.isNotBlank(request.getCreatedFrom())) {
            ZonedDateTime createdFrom = DateTimeMapper.INSTANCE.toZonedDateTimeTrimTime(request.getCreatedFrom());
            filters = Filters.and(filters, Filters.gte(UserFields.CREATED_AT.getName(), DateTimeMapper.INSTANCE.toDate(createdFrom)));
        }
        if(StringUtils.isNotBlank(request.getCreatedTo())) {
            ZonedDateTime createdTo = DateTimeMapper.INSTANCE.toZonedDateTimeTrimTime(request.getCreatedTo());
            createdTo = createdTo.plusDays(1);
            filters = Filters.and(filters, Filters.lt(UserFields.CREATED_AT.getName(), DateTimeMapper.INSTANCE.toDate(createdTo)));
        }
        if(request.getStatus() != null) {
            filters = Filters.and(filters, Filters.eq(UserFields.STATUS.getName(), request.getStatus()));
        }

        MongoCollection<Document> userCollection = mongoDatabase.getCollection(COLLECTION_NAME);

        List<UserEntity> records = new ArrayList<>();
        try(MongoCursor<Document> cursor = userCollection.find(filters)
                .limit(request.getLimit() + 1)
                .skip(request.getOffset())
                .sort(Indexes.descending(UserFields.CREATED_AT.getName()))
                .iterator()) {

            while(cursor.hasNext()) {
                Document userDocument = cursor.next();
                records.add(UserMapper.INSTANCE.toEntity(userDocument));
            }
        }

        boolean nexPage = false;
        if(records.size() > request.getLimit()) {
            nexPage = true;
            records = records.stream().limit(request.getLimit()).toList();
        }

        return new SearchResponse<>(records, request.getLimit(), request.getOffset(), nexPage);
    }

    public void activateOrganizationAdmin(String organizationId, String passwordHash) {

        MongoCollection<Document> userCollection = mongoDatabase.getCollection(COLLECTION_NAME);

        Bson filter = Filters.eq(UserFields.ORGANIZATION_ID.getName(), organizationId);
        Bson updates = Updates.combine(
                Updates.set(UserFields.STATUS.getName(), Status.ACTIVE.getName()),
                Updates.set(UserFields.PASSWORD.getName(), passwordHash));

        userCollection.updateOne(filter, updates);
    }

    public void updateStatus(String id, Status newStatus) {

        MongoCollection<Document> userCollection = mongoDatabase.getCollection(COLLECTION_NAME);

        Bson filter = Filters.eq(UserFields.ID.getName(), new ObjectId(id));

        Bson updates = Updates.combine(
                Updates.set(UserFields.STATUS.getName(), newStatus.getName()));

        userCollection.updateOne(filter, updates);
    }

    public void update(String id, UserEntity entity) {

        MongoCollection<Document> userCollection = mongoDatabase.getCollection(COLLECTION_NAME);

        Bson filter = Filters.eq(UserFields.ID.getName(), new ObjectId(id));
        Bson updates = Updates.combine(
                Updates.set(UserFields.FIRST_NAME.getName(), entity.getFirstName()),
                Updates.set(UserFields.LAST_NAME.getName(), entity.getLastName()),
                Updates.set(UserFields.PERMISSIONS.getName(), entity.getPermissions()));

        userCollection.updateOne(filter, updates);
    }

    public void completeUserRegistration(String id, String passwordHash) {

        MongoCollection<Document> userCollection = mongoDatabase.getCollection(COLLECTION_NAME);

        Bson filter = Filters.eq(UserFields.ID.getName(), new ObjectId(id));
        Bson updates = Updates.combine(
                Updates.set(UserFields.STATUS.getName(), Status.ACTIVE.getName()),
                Updates.set(UserFields.PASSWORD.getName(), passwordHash));

        userCollection.updateOne(filter, updates);
    }

    public void updateResetPasswordToken(String id, String tokenHash, Date tokenValidTo) {

        MongoCollection<Document> userCollection = mongoDatabase.getCollection(COLLECTION_NAME);

        Bson filter = Filters.eq(UserFields.ID.getName(), new ObjectId(id));
        Bson updates = Updates.combine(
                Updates.set(UserFields.RESET_PASSWORD_TOKEN.getName(), tokenHash),
                Updates.set(UserFields.RESET_PASSWORD_TOKEN_VALID_TO.getName(), tokenValidTo));

        userCollection.updateOne(filter, updates);
    }

    public void completeResetPassword(String id, String passwordHash) {

        MongoCollection<Document> userCollection = mongoDatabase.getCollection(COLLECTION_NAME);

        Bson filter = Filters.eq(UserFields.ID.getName(), new ObjectId(id));
        Bson updates = Updates.combine(
                Updates.set(UserFields.PASSWORD.getName(), passwordHash),
                Updates.set(UserFields.RESET_PASSWORD_TOKEN.getName(), null),
                Updates.set(UserFields.RESET_PASSWORD_TOKEN_VALID_TO.getName(), null));

        userCollection.updateOne(filter, updates);
    }
}
