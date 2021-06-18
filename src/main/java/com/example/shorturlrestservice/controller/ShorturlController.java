package com.example.shorturlrestservice.controller;

import com.example.shorturlrestservice.exception.UrlInvalidException;
import com.example.shorturlrestservice.exception.UrlNotFoundException;
import com.example.shorturlrestservice.model.CreateReqVO;
import com.example.shorturlrestservice.model.CreateRespVO;
import com.example.shorturlrestservice.model.UrlMapper;
import com.example.shorturlrestservice.service.IUrlMapperService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@NoArgsConstructor
@Slf4j
public class ShorturlController {

    @Autowired
    private IUrlMapperService urlMapperService;

    @PostMapping(value = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CreateRespVO create(@RequestBody CreateReqVO createReqVO) {
        try {
            UrlMapper urlMapper = urlMapperService.createUrlMapper(createReqVO.getUrl());
            String shortUrl = linkTo(methodOn(ShorturlController.class)
                    .retrievePath(urlMapper.getId()))
                    .withSelfRel()
                    .getHref();
            return new CreateRespVO(shortUrl);
        } catch (UrlInvalidException ex) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Invalid URL provided", ex);
        }
    }

    @GetMapping(value = "/{path}")
    public RedirectView retrievePath(@PathVariable String path) {
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