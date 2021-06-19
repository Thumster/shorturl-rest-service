package com.example.shorturlrestservice;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.shorturlrestservice.controller.ShorturlController;
import com.example.shorturlrestservice.repository.UrlMapperRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@AutoConfigureMockMvc
class ShorturlRestServiceApplicationTests {

    @Autowired
    private ShorturlController controller;

    @MockBean
    private UrlMapperRepository repository;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

}
