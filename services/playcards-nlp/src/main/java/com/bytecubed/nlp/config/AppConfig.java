package com.bytecubed.nlp.config;

import org.bots4j.wit.WitClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public WitClient witClient(@Value("${wit.ai.key}") String key){
        return new WitClient(key);
    }

}
