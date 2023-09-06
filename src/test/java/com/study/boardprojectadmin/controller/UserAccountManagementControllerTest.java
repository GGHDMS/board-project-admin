package com.study.boardprojectadmin.controller;

import com.study.boardprojectadmin.config.SecurityConfig;
import com.study.boardprojectadmin.dto.UserAccountDto;
import com.study.boardprojectadmin.service.UserAccountManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("컨트롤러 - 회원 관리")
@WebMvcTest(UserAccountManagementController.class)
@ContextConfiguration(classes = {UserAccountManagementController.class, SecurityConfig.class})
class UserAccountManagementControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserAccountManagementService userAccountManagementService;

    @DisplayName("[View][GET] 회원 관리 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingUserAccountsManagementView_thenReturnsUserAccountsManagementView() throws Exception {
        //given
        given(userAccountManagementService.getUserAccounts()).willReturn(List.of());

        //when & then
        mvc.perform(get("/management/user-accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/user-accounts"))
                .andExpect(model().attribute("userAccounts", List.of()));

        then(userAccountManagementService).should().getUserAccounts();
    }

    @DisplayName("[View][GET] 회원 1개 - 정상 호출")
    @Test
    public void givenUserAccountId_whenRequestingUserAccountManagementView_thenReturnsUserAccountManagementView() throws Exception {
        //given
        String userId = "hsm";
        UserAccountDto userAccountDto = createUserAccountDto(userId, "hsm");
        given(userAccountManagementService.getUserAccount(userId)).willReturn(userAccountDto);

        //when & then
        mvc.perform(get("/management/user-accounts/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.nickname").value(userAccountDto.getNickname()));

        then(userAccountManagementService).should().getUserAccounts();
    }

    @DisplayName("[View][Post] 회원 삭제 - 정상 호출")
    @Test
    public void givenUserAccountId_whenRequestingDelete_thenRedirectToUserAccountManagementView() throws Exception {
        //given
        String userId = "hsm";
        willDoNothing().given(userAccountManagementService).deleteUserAccount(userId);

        //when & then
        mvc.perform(
                        post("/management/user-accounts/" + userId)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/user-accounts"))
                .andExpect(redirectedUrl("/management/user-accounts"));

        then(userAccountManagementService).should().deleteUserAccount(userId);
    }


    private UserAccountDto createUserAccountDto(String userId, String nickname) {
        return UserAccountDto.of(
                userId,
                "hsm@email.com",
                nickname,
                "test memo"
        );
    }
}
