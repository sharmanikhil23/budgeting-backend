package com.budgeting.backend.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class DbConfig {

    @Bean
    @Profile("dev")
    @Order(0)
    public CommandLineRunner cleanDatabase(MongoTemplate mongoTemplate) {
        return args -> {
            System.out.println("-----------------------------------------");
            System.out.println("DEV PROFILE ACTIVE: Dropping database...");
            mongoTemplate.getDb().drop();
            System.out.println("Database 'budgeting' wiped clean.");
            System.out.println("-----------------------------------------");
        };
    }
}
