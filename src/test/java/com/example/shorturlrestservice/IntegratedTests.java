package com.example.shorturlrestservice;

import com.example.shorturlrestservice.model.CreateReqVO;
import com.example.shorturlrestservice.model.UrlMapper;
import com.example.shorturlrestservice.repository.UrlMapperRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@AutoConfigureMockMvc
class IntegratedTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlMapperRepository repository;

    @Test
    public void givenUrl_whenCreateShortUrl_thenReturnShortUrl() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String urlToShorten = "http://test.com";
        String shortenedId = "abcdefg";

        UrlMapper resultUrlMapper = new UrlMapper(shortenedId, urlToShorten, LocalDateTime.now());


        Mockito.when(repository.save(Mockito.any(UrlMapper.class))).thenReturn(resultUrlMapper);

        CreateReqVO createReq = new CreateReqVO(urlToShorten);
        this.mockMvc.perform(
                MockMvcRequestBuilders.post(
                        "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("shortUrl")));
    }

    @Test
    public void givenInvalidUrl_whenCreateShortUrl_thenThrowError() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String urlToShorten = "test.com";

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
    public void givenShortUrlId_whenRetrievePath_thenRedirectToPath() throws Exception {
        String urlToShorten = "http://test.com";
        String shortenedId = "abcdefg";

        UrlMapper resultUrlMapper = new UrlMapper(shortenedId, urlToShorten, LocalDateTime.now());

        Mockito.when(repository.findById(shortenedId)).thenReturn(Optional.of(resultUrlMapper));

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

        Mockito.when(repository.findById(shortenedId)).thenReturn(Optional.empty());

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/{id}", shortenedId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
