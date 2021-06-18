package com.example.shorturlrestservice;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.shorturlrestservice.controller.ShorturlController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class ShorturlRestServiceApplicationTests {

    @Autowired
    private ShorturlController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

}
