package com.bytecubed.nlp.config;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.bytecubed")
public class PersistenceConfig extends AbstractMongoConfiguration {
    @Value("${persistence.mongo.url}") String url;
    @Value("${persistence.mongo.port}") int port;

    @Override
    protected String getDatabaseName() {
        return "playcards";
    }

    @Override
    public MongoClient mongoClient( ) {
        return new MongoClient(url, port);
    }

    @Override
    protected String getMappingBasePackage() {
        return "com.bytecubed";
    }
}
