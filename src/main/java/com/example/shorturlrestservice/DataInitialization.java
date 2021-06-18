package com.example.shorturlrestservice;

import com.example.shorturlrestservice.service.IUrlMapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DataInitialization {

    @Bean
    CommandLineRunner initDatabase(IUrlMapperService urlMapperService) {
        return args -> {
            log.info("Preloaded " + urlMapperService.createUrlMapper("https://www.google.com/").getId());
            log.info("Preloaded " + urlMapperService.createUrlMapper("https://www.facebook.com/").getId());
        };
    }
}
