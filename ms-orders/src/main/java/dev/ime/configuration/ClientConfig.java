package dev.ime.configuration;

import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ime.client.CustomErrorDecoder;
import feign.codec.ErrorDecoder;

@Configuration
public class ClientConfig {

    @Bean
    ErrorDecoder errorDecoder(ObjectMapper objectMapper, Logger logger) {
        return new CustomErrorDecoder( objectMapper, logger);
    }
}
