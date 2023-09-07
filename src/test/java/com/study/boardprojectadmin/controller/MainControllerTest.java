package com.study.boardprojectadmin.controller;

import com.study.boardprojectadmin.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 루트 컨트롤러")
@WebMvcTest(MainController.class)
@ContextConfiguration(classes = {MainController.class, TestSecurityConfig.class})
class MainControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("[View][GET] 루트 페이지 -> 게시글 관리 페이지 Forwarding")
    @Test
    public void givenNothing_whenRequestingRootView_thenForwardsToArticleManagementView() throws Exception{
        //given

        //when & then
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/management/articles"))
                .andExpect(forwardedUrl("/management/articles"));
    }
}
