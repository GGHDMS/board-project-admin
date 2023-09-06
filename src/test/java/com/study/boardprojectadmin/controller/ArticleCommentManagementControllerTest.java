package com.study.boardprojectadmin.controller;

import com.study.boardprojectadmin.config.SecurityConfig;
import com.study.boardprojectadmin.domain.constant.RoleType;
import com.study.boardprojectadmin.dto.ArticleCommentDto;
import com.study.boardprojectadmin.dto.UserAccountDto;
import com.study.boardprojectadmin.service.ArticleCommentManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("컨트롤러 - 댓글 관리")
@WebMvcTest(ArticleCommentManagementController.class)
@ContextConfiguration(classes = {ArticleCommentManagementController.class, SecurityConfig.class})
class ArticleCommentManagementControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArticleCommentManagementService articleCommentManagementService;

    @DisplayName("[View][GET] 댓글 관리 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleCommentsManagementView_thenReturnsArticleCommentsManagementView() throws Exception {
        //given
        given(articleCommentManagementService.getArticleComments()).willReturn(List.of());

        //when & then
        mvc.perform(get("/management/article-comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/article-comments"))
                .andExpect(model().attribute("comments", List.of()));

        then(articleCommentManagementService).should().getArticleComments();
    }

    @DisplayName("[View][GET] 댓글 1개 - 정상 호출")
    @Test
    public void givenCommentId_whenRequestingCommentManagementView_thenReturnCommentManagementView() throws Exception {
        //given
        Long commentId = 1L;
        ArticleCommentDto articleCommentDto = createArticleCommentDto("content");
        given(articleCommentManagementService.getArticleComment(commentId)).willReturn(articleCommentDto);

        //when & then
        mvc.perform(get("/management/article-comments/" + commentId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.content").value(articleCommentDto.getContent()))
                .andExpect(jsonPath("$.userAccount.nickname").value(articleCommentDto.getUserAccount().getNickname()));

        then(articleCommentManagementService.getArticleComment(commentId)).should();
    }

    @DisplayName("[View][Post] 댓글 삭제 - 정상 호출")
    @Test
    public void givenCommentId_whenRequestingDelete_thenRedirectToCommentManagementView() throws Exception {
        //given
        Long commentId = 1L;
        willDoNothing().given(articleCommentManagementService).deleteArticleComment(commentId);

        //when & then
        mvc.perform(
                        post("/management/article-comments/" + commentId)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/article-comments"))
                .andExpect(redirectedUrl("/management/article-comments"));

        then(articleCommentManagementService).should().deleteArticleComment(commentId);
    }

    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                null,
                content,
                LocalDateTime.now(),
                "Hsm",
                LocalDateTime.now(),
                "Hsm"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "hsmTest",
                Set.of(RoleType.ADMIN),
                "hsm@email.com",
                "hsm-test",
                "test memo"
        );
    }

}
