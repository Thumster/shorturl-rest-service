package com.example.shorturlrestservice.controller;

import com.example.shorturlrestservice.exception.UrlInvalidException;
import com.example.shorturlrestservice.exception.UrlNotFoundException;
import com.example.shorturlrestservice.model.UrlMapper;
import com.example.shorturlrestservice.service.IUrlMapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class ShorturlController {

    @Autowired
    private IUrlMapperService urlMapperService;

    @PostMapping(value = "/create")
    public String create(@RequestParam String originalUrl, HttpServletRequest request) {
        try {
            UrlMapper urlMapper = urlMapperService.createUrlMapper(originalUrl);
            return urlMapper.getId();
        } catch (UrlInvalidException ex) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Invalid URL provided", ex);
        }
    }

    @GetMapping(value = "/{path}")
    public RedirectView retrievePath(@PathVariable String path) {

//        List<UrlMapper> urlMappers = repository.findAll();
//        for (int i = 0; i < urlMappers.size(); i++) {
//            log.info(String.format("Retrieved %d\n\tid: %s\n\toriginal value: %s\n\tisValid: %s",
//                    i + 1, urlMappers.get(i).getId(), urlMappers.get(i).getOriginalUrl(), urlValidator.isValid(urlMappers.get(i).getOriginalUrl())));
//        }
        try {
            UrlMapper urlMapper = urlMapperService.findById(path);
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(urlMapper.getOriginalUrl());
            return redirectView;
        } catch (UrlNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provided shortened URL not found", ex);
        }
    }
}