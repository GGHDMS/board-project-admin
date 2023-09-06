package com.study.boardprojectadmin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.boardprojectadmin.dto.UserAccountDto;
import com.study.boardprojectadmin.dto.properties.ProjectProperties;
import com.study.boardprojectadmin.dto.response.UserAccountClientResponse;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@ActiveProfiles("test")
@DisplayName("비즈니스 로직 - 회원 관리")
class UserAccountManagementServiceTest {

    //    @Disabled("실제 API 호출 결과 관찰용이므로 평사시엔 비활성화 한다.")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {
        @Autowired
        private UserAccountManagementService sut;

        @DisplayName("회원 API 를 호출하면, 회원 정보를 가져온다")
        @Test
        void givenNothing_whenCallingUserAccountApi_thenReturnUserAccountList() {
            //given

            //when
            List<UserAccountDto> result = sut.getUserAccounts();

            //then
            System.out.println(result.stream().findFirst());
            assertThat(result).isNotNull();
        }

    }

    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(UserAccountManagementService.class)
    @Nested
    class RestTemplateTest {
        @Autowired
        private UserAccountManagementService sut;

        @Autowired
        private ProjectProperties projectProperties;

        @Autowired
        private MockRestServiceServer service;

        @Autowired
        private ObjectMapper mapper;

        @DisplayName("회원 목록 API 를 호출하면, 회원들을 가져온다.")
        @Test
        void givenNothing_whenCallingUserAccountApi_thenReturnsUserAccountList() throws Exception {

            //given
            UserAccountDto expectedUserAccount = createUserAccountDto("hsm", "hsm");
            UserAccountClientResponse expectedResponse = UserAccountClientResponse.of(List.of(expectedUserAccount));
            service
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/userAccounts?size=10000"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));

            //when
            List<UserAccountDto> result = sut.getUserAccounts();

            //then
            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("id", expectedUserAccount.getUserId())
                    .hasFieldOrPropertyWithValue("nickname", expectedUserAccount.getNickname());

            service.verify();
        }

        @DisplayName("회원 ID 와 함께 회원 API 를 호출하면, 회원을 가져온다")
        @Test
        void givenUserAccountId_whenCallingUserAccountApi_thenReturnUserAccount() throws Exception {

            //given
            String userId = "hsm";
            UserAccountDto expectedUserAccount = createUserAccountDto("hsm", "hsm");

            service
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/userAccounts/" + userId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedUserAccount),
                            MediaType.APPLICATION_JSON
                    ));

            //when
            UserAccountDto result = sut.getUserAccount(userId);

            //then
            assertThat(result)
                    .hasFieldOrPropertyWithValue("id", expectedUserAccount.getUserId())
                    .hasFieldOrPropertyWithValue("nickname", expectedUserAccount.getNickname());

            service.verify();
        }

        @DisplayName("회원 ID와 함께 회원 삭제 API 를 호출하면, 회원을 삭제한다")
        @Test
        void givenUserAccountId_whenCallingDeleteUserAccountApi_thenDeleteUserAccount() throws Exception {
            //given
            String userId = "hsm";

            service
                    .expect(requestTo(projectProperties.getBoard().getUrl() + "/api/userAccounts/" + userId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            //when
            sut.deleteUserAccount(userId);

            //then
            service.verify();
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
}
