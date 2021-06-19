package com.example.shorturlrestservice;

import com.example.shorturlrestservice.exception.UrlInvalidException;
import com.example.shorturlrestservice.exception.UrlNotFoundException;
import com.example.shorturlrestservice.model.CreateReqVO;
import com.example.shorturlrestservice.model.UrlMapper;
import com.example.shorturlrestservice.service.UrlMapperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest
class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlMapperService urlMapperService;

    @Test
    public void givenUrl_whenCreateShortUrl_thenReturnShortUrl() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String urlToShorten = "http://test.com";
        String shortenedId = "abcdefg";

        UrlMapper resultUrlMapper = new UrlMapper(shortenedId, urlToShorten, LocalDateTime.now());


        Mockito.when(urlMapperService.createUrlMapper(urlToShorten)).thenReturn(resultUrlMapper);

        CreateReqVO createReq = new CreateReqVO(urlToShorten);
        this.mockMvc.perform(
                MockMvcRequestBuilders.post(
                        "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(shortenedId)));
    }

    @Test
    public void givenInvalidUrl_whenCreateShortUrl_thenThrowError() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String urlToShorten = "test.com";

        Mockito.when(urlMapperService.createUrlMapper(urlToShorten)).thenThrow(new UrlInvalidException(""));

        CreateReqVO createReq = new CreateReqVO(urlToShorten);
        this.mockMvc.perform(
                MockMvcRequestBuilders.post(
                        "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    public void givenInvalidMediaType_whenCreateShortUrl_thenThrowError() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String requestBody = "{}";

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(
                        "/create")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void givenShortUrlId_whenRetrievePath_thenRedirectToPath() throws Exception {
        String urlToShorten = "http://test.com";
        String shortenedId = "abcdefg";

        UrlMapper resultUrlMapper = new UrlMapper(shortenedId, urlToShorten, LocalDateTime.now());

        Mockito.when(urlMapperService.findById(shortenedId)).thenReturn(resultUrlMapper);

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/{id}", shortenedId))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl(urlToShorten));
    }

    @Test
    public void givenShortInvalidUrlId_whenRetrievePath_thenThrowError() throws Exception {
        String shortenedId = "abcdefg";

        Mockito.when(urlMapperService.findById(shortenedId)).thenThrow(new UrlNotFoundException(""));

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/{id}", shortenedId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
