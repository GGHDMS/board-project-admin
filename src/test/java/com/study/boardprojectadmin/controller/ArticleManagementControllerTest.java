package com.study.boardprojectadmin.controller;

import com.study.boardprojectadmin.config.TestSecurityConfig;
import com.study.boardprojectadmin.dto.ArticleDto;
import com.study.boardprojectadmin.dto.UserAccountDto;
import com.study.boardprojectadmin.service.ArticleManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("컨트롤러 - 게시글 관리")
@WebMvcTest(ArticleManagementController.class)
@ContextConfiguration(classes = {ArticleManagementController.class, TestSecurityConfig.class})
class ArticleManagementControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArticleManagementService articleManagementService;

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[View][GET] 게시글 관리 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleManagementView_thenReturnsArticleManagementView() throws Exception {
        //given
        given(articleManagementService.getArticles()).willReturn(List.of());

        //when & then
        mvc.perform(get("/management/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/articles"))
                .andExpect(model().attribute("articles", List.of()));

        then(articleManagementService).should().getArticles();
    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[data][GET] 게시글 1개 - 정상 호출")
    @Test
    public void givenArticleId_whenRequestingArticleManagementView_thenReturnsArticleManagementView() throws Exception {
        //given
        Long articleId = 1L;
        ArticleDto articleDto = createArticleDto("title", "content");
        given(articleManagementService.getArticle(articleId)).willReturn(articleDto);

        //when & then
        mvc.perform(get("/management/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(articleId))
                .andExpect(jsonPath("$.title").value(articleDto.getTitle()))
                .andExpect(jsonPath("$.content").value(articleDto.getContent()))
                .andExpect(jsonPath("$.userAccount.nickname").value(articleDto.getUserAccount().getNickname()));

        then(articleManagementService).should().getArticle(articleId);
    }

    @WithMockUser(username = "tester", roles = "MANAGER")
    @DisplayName("[data][Post] 게시글 삭제 - 정상 호출")
    @Test
    public void givenArticleId_whenRequestingDelete_thenRedirectToArticleManagementView() throws Exception {
        //given
        Long articleId = 1L;
        willDoNothing().given(articleManagementService).deleteArticle(articleId);

        //when & then
        mvc.perform(
                        post("/management/articles/" + articleId)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/articles"))
                .andExpect(redirectedUrl("/management/articles"));

        then(articleManagementService).should().deleteArticle(articleId);
    }


    private ArticleDto createArticleDto(String title, String content) {
        return ArticleDto.of(
                1L,
                createUserAccountDto(),
                title,
                content,
                null,
                LocalDateTime.now(),
                "Hsm",
                LocalDateTime.now(),
                "Hsm"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "hsmTest",
                "hsm@email.com",
                "hsm-test",
                "test memo"
        );
    }
}
