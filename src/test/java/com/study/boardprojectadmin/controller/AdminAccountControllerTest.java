package com.study.boardprojectadmin.controller;

import com.study.boardprojectadmin.config.SecurityConfig;
import com.study.boardprojectadmin.domain.constant.RoleType;
import com.study.boardprojectadmin.dto.AdminAccountDto;
import com.study.boardprojectadmin.service.AdminAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 어드민 회원")
@WebMvcTest(AdminAccountController.class)
@ContextConfiguration(classes = {AdminAccountController.class, SecurityConfig.class})
class AdminAccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AdminAccountService adminAccountService;

    @BeforeTestMethod
    public void securitySetup() {
        given(adminAccountService.searchUser(anyString()))
                .willReturn(Optional.of(createAdminAccountDto()));

        given(adminAccountService.saveUser(anyString(), anyString(), anySet(), anyString(), anyString(), anyString()))
                .willReturn(createAdminAccountDto());

    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[View][GET] 어드민 회원 페이지 - 정상 호출")
    @Test
    public void givenAuthorizedUser_whenRequestingAdminMembersView_thenReturnsAdminMembersView() throws Exception {
        //given

        //when & then
        mvc.perform(get("/admin/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/members"));
    }

    @WithMockUser(username = "tester", roles = "USER")
    @Test
    @DisplayName("[data][GET] 어드민 회원 리스트 - 정상 호출")
    public void givenAuthorizedUser_whenRequestingAdminMembers_thenReturnAdminMembers() throws Exception {
        //given

        //when & then
        mvc.perform(get("/api/admin/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        then(adminAccountService).should().users();
    }

    @WithMockUser(username = "tester", roles = "MANAGER")
    @Test
    @DisplayName("[data][DELETE] 어드민 회원 삭제 - 정상 호출")
    public void givenAuthorizedUser_whenDeletingAdminMember_thenDeleteAdminMember() throws Exception {
        //given
        String userId = "hsm";
        willDoNothing().given(adminAccountService).deleteUser(userId);

        //when & then
        mvc.perform(
                        delete("/api/admin/members/" + userId)
                                .with(csrf())
                )
                .andExpect(status().isNoContent());
        then(adminAccountService).should().deleteUser(userId);

    }

    private AdminAccountDto createAdminAccountDto() {
        return AdminAccountDto.of(
                "hsmTest",
                "pw",
                Set.of(RoleType.USER),
                "hsm@email.com",
                "hsm-test",
                "test memo"
        );
    }


}
