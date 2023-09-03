package com.study.boardprojectadmin.controller;

import com.study.boardprojectadmin.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 회원 관리")
@WebMvcTest(UserAccountManagementController.class)
@ContextConfiguration(classes = {UserAccountManagementController.class, SecurityConfig.class})
class UserAccountManagementControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("[View][GET] 회원 관리 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingUserAccountsManagementView_thenReturnsUserAccountsManagementView() throws Exception{
        //given

        //when & then
        mvc.perform(get("/management/user-accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/user-accounts"));
    }
}
