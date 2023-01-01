package com.mealkit.controller;

import com.mealkit.config.SecurityConfig;
import com.mealkit.util.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("View 루트 컨트롤러")
@Import(TestSecurityConfig.class)
@WebMvcTest(MainController.class)
class MainControllerTest {

    public final MockMvc mvc;

    public MainControllerTest(@Autowired MockMvc mvc){
        this.mvc=mvc;
    }

    @Test
    void givenNothing_whenRequestRootPage_returnRedirectToAdminPostPage() throws Exception {

        // Given

        // When
        mvc.perform(get("/")).andExpect(status().is3xxRedirection());

        // Then
    }

}