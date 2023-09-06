package com.study.boardprojectadmin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.boardprojectadmin.domain.constant.RoleType;
import com.study.boardprojectadmin.dto.ArticleCommentDto;
import com.study.boardprojectadmin.dto.UserAccountDto;
import com.study.boardprojectadmin.dto.properties.ProjectProperties;
import com.study.boardprojectadmin.dto.response.ArticleCommentClientResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@DisplayName("비즈니스 로직 - 댓글 관리")
class ArticleCommentManagementServiceTest {
    
    //    @Disabled("실제 API 호출 결과 관찰용이므로 평사시엔 비활성화 한다.")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {
        @Autowired
        private ArticleCommentManagementService sut;

        @DisplayName("댓글 API 를 호출하면, 댓글을 가져온다")
        @Test
        void givenNothing_whenCallingCommentApi_thenReturnCommentList() {
            //given

            //when
            List<ArticleCommentDto> result = sut.getArticleComments();

            //then
            System.out.println(result.stream().findFirst());
            assertThat(result).isNotNull();
        }

    }

    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(ArticleCommentManagementService.class)
    @Nested
    class RestTemplateTest {
        @Autowired
        private ArticleCommentManagementService sut;

        @Autowired
        private ProjectProperties projectProperties;

        @Autowired
        private MockRestServiceServer service;

        @Autowired
        private ObjectMapper mapper;

        @DisplayName("댓글 목록 API 를 호출하면, 댓글들을 가져온다.")
        @Test
        void givenNothing_whenCallingCommentApi_thenReturnsCommentList() throws Exception {

            //given
            ArticleCommentDto expectedComment = createArticleCommentDto("댓글");
            ArticleCommentClientResponse expectedResponse = ArticleCommentClientResponse.of(List.of(expectedComment));
            service
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/articleComments?size=10000"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));

            //when
            List<ArticleCommentDto> result = sut.getArticleComments();

            //then
            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("id", expectedComment.getId())
                    .hasFieldOrPropertyWithValue("content", expectedComment.getContent())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedComment.getUserAccount().getNickname());

            service.verify();
        }

        @DisplayName("댓글 ID 와 함께 댓글 API 를 호출하면, 댓글을 가져온다")
        @Test
        void givenCommentId_whenCallingCommentApi_thenReturnComment() throws Exception {
            //given

            Long commentId = 1L;
            ArticleCommentDto expectedComment = createArticleCommentDto("내용");

            service
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/articleComments/" + commentId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedComment),
                            MediaType.APPLICATION_JSON
                    ));

            //when
            ArticleCommentDto result = sut.getArticleComment(commentId);

            //then
            assertThat(result)
                    .hasFieldOrPropertyWithValue("id", expectedComment.getId())
                    .hasFieldOrPropertyWithValue("content", expectedComment.getContent())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedComment.getUserAccount().getNickname());

            service.verify();
        }

        @DisplayName("댓글 Id와 함께 댓글 삭제 API 를 호출하면, 댓글을 삭제한다")
        @Test
        void givenCommentId_whenCallingDeleteCommentApi_thenDeleteComment() throws Exception {
            //given
            Long commentId = 1L;

            service
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/articleComments/" + commentId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            //when
            sut.deleteArticleComment(commentId);

            //then
            service.verify();
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
}
