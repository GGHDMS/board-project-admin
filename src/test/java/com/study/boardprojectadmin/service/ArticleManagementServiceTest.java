package com.study.boardprojectadmin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.boardprojectadmin.domain.constant.RoleType;
import com.study.boardprojectadmin.dto.ArticleDto;
import com.study.boardprojectadmin.dto.UserAccountDto;
import com.study.boardprojectadmin.dto.properties.ProjectProperties;
import com.study.boardprojectadmin.dto.response.ArticleClientResponse;
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
@DisplayName("비즈니스 로직 - 게시글 관리")
class ArticleManagementServiceTest {

    //    @Disabled("실제 API 호출 결과 관찰용이므로 평사시엔 비활성화 한다.")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {
        @Autowired
        private ArticleManagementService sut;

        @DisplayName("게시글 API 를 호출하면, 게시글을 가져온다")
        @Test
        void givenNothing_whenCallingArticleApi_thenReturnArticleList() {
            //given

            //when
            List<ArticleDto> result = sut.getArticles();

            //then
            System.out.println(result.stream().findFirst());
            assertThat(result).isNotNull();
        }

    }

    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(ArticleManagementServiceTest.class)
    @Nested
    class RestTemplateTest {
        @Autowired
        private ArticleManagementService sut;

        @Autowired
        private ProjectProperties projectProperties;

        @Autowired
        private MockRestServiceServer service;

        @Autowired
        private ObjectMapper mapper;

        @DisplayName("게시글 목록 API 를 호출하면, 게시글들을 가져온다.")
        @Test
        void givenNothing_whenCallingArticleApi_thenReturnsArticleList() throws Exception {

            //given
            ArticleDto expectedArticle = createArticleDto("제목", "글");
            ArticleClientResponse expectedResponse = ArticleClientResponse.of(List.of(expectedArticle));
            service
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/articles?size=10000"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));

            //when
            List<ArticleDto> result = sut.getArticles();

            //then
            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("id", expectedArticle.getId())
                    .hasFieldOrPropertyWithValue("title", expectedArticle.getTitle())
                    .hasFieldOrPropertyWithValue("content", expectedArticle.getContent())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticle.getUserAccount().getNickname());

            service.verify();
        }

        @DisplayName("게시판 ID 와 함께 게시글 API 를 호출하면, 게시글을 가져온다")
        @Test
        void givenArticleId_whenCallingArticleApi_thenReturnArticle() throws Exception {
            //given

            Long articleId = 1L;
            ArticleDto expectedArticle = createArticleDto("게시판", "내용");

            service
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/articles/" + articleId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedArticle),
                            MediaType.APPLICATION_JSON
                    ));

            //when
            ArticleDto result = sut.getArticle(articleId);

            //then
            assertThat(result)
                    .hasFieldOrPropertyWithValue("id", expectedArticle.getId())
                    .hasFieldOrPropertyWithValue("title", expectedArticle.getTitle())
                    .hasFieldOrPropertyWithValue("content", expectedArticle.getContent())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticle.getUserAccount().getNickname());

            service.verify();
        }

        @DisplayName("게시글 Id와 함께 게시글 삭제 API 를 호출하면, 게시글을 삭제한다")
        @Test
        void givenArticleId_whenCallingDeleteArticleApi_thenDeleteArticle() throws Exception {
            //given
            Long articleId = 1L;

            service
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/articles/" + articleId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            //when
            sut.deleteArticle(articleId);

            //then
            service.verify();
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
                    "pw",
                    Set.of(RoleType.ADMIN),
                    "hsm@email.com",
                    "hsm-test",
                    "test memo"
            );
        }
    }
}
