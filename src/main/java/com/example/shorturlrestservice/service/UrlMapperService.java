package com.example.shorturlrestservice.service;

import com.example.shorturlrestservice.exception.UrlInvalidException;
import com.example.shorturlrestservice.exception.UrlNotFoundException;
import com.example.shorturlrestservice.model.UrlMapper;
import com.example.shorturlrestservice.repository.UrlMapperRepository;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class UrlMapperService implements IUrlMapperService {

    @Autowired
    UrlMapperRepository repository;

    @Override
    public UrlMapper createUrlMapper(String originalUrl) throws UrlInvalidException {
        log.info("Creating shortened URL for: " + originalUrl);
        UrlValidator urlValidator = new UrlValidator();
        boolean isValid = urlValidator.isValid(originalUrl);
        if (!isValid) {
            log.error("Provided URL : " + originalUrl + " is not valid");
            throw new UrlInvalidException("Provided URL : " + originalUrl + " is not valid");
        }

        String id = Hashing.murmur3_32().hashString(originalUrl, StandardCharsets.UTF_8).toString();
        UrlMapper urlMapper;
        try {
            log.info("Checking if shortened URL already exists");
            urlMapper = findById(id);
            log.info("URL already exists: updating created date time");
            urlMapper.setCreatedDateTime(LocalDateTime.now());
        } catch (UrlNotFoundException ex) {
            log.info("URL does not exist: creating URL mapper");
            urlMapper = new UrlMapper(id, originalUrl, LocalDateTime.now());
        }

        repository.save(urlMapper);
        log.info("Successfully created new shortened URL: " + id);
        return urlMapper;
    }

    @Override
    public List<UrlMapper> retrieveAllUrlMapper(){
        return repository.findAll();
    }

    @Override
    public UrlMapper findById(String id) throws UrlNotFoundException {
        return repository.findById(id)
                .orElseThrow(() ->
                        new UrlNotFoundException("Unable to find provided shortened URL")
                );
    }
}
