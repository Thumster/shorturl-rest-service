package com.example.shorturlrestservice.service;

import com.example.shorturlrestservice.exception.UrlInvalidException;
import com.example.shorturlrestservice.exception.UrlNotFoundException;
import com.example.shorturlrestservice.model.UrlMapper;

public interface IUrlMapperService {

    UrlMapper createUrlMapper(String originalUrl) throws UrlInvalidException;

    UrlMapper findById(String id) throws UrlNotFoundException;
}
