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

@DisplayName("View 컨트롤러 - 어드민 회원")
@WebMvcTest(AdminUserAccountController.class)
@ContextConfiguration(classes = {AdminUserAccountController.class, SecurityConfig.class})
class AdminUserAccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("[View][GET] 어드민 회원 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingAdminMembersView_thenReturnsAdminMembersView() throws Exception{
        //given

        //when & then
        mvc.perform(get("/admin/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/members"));
    }

}
