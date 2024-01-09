package com.github.flexca.sbae.backend.common.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Value("${database.url}")
    private String connectionString;

    @Value("${database.schema}")
    private String databaseSchema;

    @Bean
    public MongoDatabase mongoDatabase() {
        MongoClient mongoClient = MongoClients.create(connectionString);
        return mongoClient.getDatabase(databaseSchema);
    }
}
