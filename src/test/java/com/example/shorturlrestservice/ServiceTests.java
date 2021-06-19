package com.example.shorturlrestservice;

import com.example.shorturlrestservice.exception.UrlInvalidException;
import com.example.shorturlrestservice.exception.UrlNotFoundException;
import com.example.shorturlrestservice.model.UrlMapper;
import com.example.shorturlrestservice.repository.UrlMapperRepository;
import com.example.shorturlrestservice.service.UrlMapperService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

@WebMvcTest(UrlMapperService.class)
class ServiceTests {

    @Autowired
    private UrlMapperService service;

    @MockBean
    private UrlMapperRepository repository;

    @Test
    public void givenUrl_whenCreateUrlMapper_thenReturnUrlMapper() throws Exception {
        String urlToShorten = "http://test.com";
        String shortenedId = "abcdefg";

        UrlMapper resultUrlMapper = new UrlMapper(shortenedId, urlToShorten, LocalDateTime.now());


        Mockito.when(repository.save(Mockito.any(UrlMapper.class))).thenReturn(resultUrlMapper);

        UrlMapper actualResult = service.createUrlMapper(urlToShorten);

        Assertions.assertThat(actualResult.getOriginalUrl())
                .isEqualTo(resultUrlMapper.getOriginalUrl());
    }

    @Test
    public void givenInvalidUrl_whenCreateUrlMapper_thenThrowError() {
        String urlToShorten = "test.com";

        Assertions.assertThatExceptionOfType(UrlInvalidException.class)
                .isThrownBy(() -> service.createUrlMapper(urlToShorten))
                .withMessageContaining(" is not valid");
    }

    @Test
    public void givenExistingUrl_whenCreateUrlMapper_thenReturnUrlMapper() throws Exception {
        String urlToShorten = "http://test.com";
        String shortenedId = "abcdefg";

        UrlMapper resultUrlMapper = new UrlMapper(shortenedId, urlToShorten, LocalDateTime.now());

        Mockito.when(repository.findById(Mockito.any(String.class))).thenReturn(Optional.of(resultUrlMapper));
        Mockito.when(repository.save(Mockito.any(UrlMapper.class))).thenReturn(resultUrlMapper);

        UrlMapper actualResult = service.createUrlMapper(urlToShorten);

        Assertions.assertThat(actualResult.getOriginalUrl())
                .isEqualTo(resultUrlMapper.getOriginalUrl());
    }

    @Test
    public void givenId_whenFindById_thenReturnUrlMapper() throws Exception {
        String urlToShorten = "http://test.com";
        String shortenedId = "abcdefg";

        UrlMapper resultUrlMapper = new UrlMapper(shortenedId, urlToShorten, LocalDateTime.now());

        Mockito.when(repository.findById(shortenedId)).thenReturn(Optional.of(resultUrlMapper));

        UrlMapper actualResult = service.findById(shortenedId);

        Assertions.assertThat(actualResult.getId())
                .isEqualTo(actualResult.getId());
    }

    @Test
    public void givenInvalidId_whenFindById_thenThrowError() {
        String shortenedId = "abcdefg";

        Mockito.when(repository.findById(shortenedId)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(UrlNotFoundException.class)
                .isThrownBy(() -> service.findById(shortenedId))
                .withMessageContaining("Unable to find provided shortened URL");
    }

}
