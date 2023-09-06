package com.study.boardprojectadmin.repository;

import com.study.boardprojectadmin.domain.AdminAccount;
import com.study.boardprojectadmin.domain.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final AdminAccountRepository adminAccountRepository;

    public JpaRepositoryTest(@Autowired AdminAccountRepository adminAccountRepository) {
        this.adminAccountRepository = adminAccountRepository;
    }

    @DisplayName("회원 정보 select 테스트")
    @Test
    void givenAdminAccount_whenSelecting_thenWorksFine() {
        //Given

        //When
        List<AdminAccount> adminAccounts = adminAccountRepository.findAll();

        //Then
        assertThat(adminAccounts)
                .isNotNull()
                .hasSize(4);
    }

    @DisplayName("회원 정보 insert 테스트")
    @Test
    void givenAdminAccount_whenInserting_thenWorksFine() {
        //Given
        long previousCount = adminAccountRepository.count();
        AdminAccount adminAccount = AdminAccount.of("test", "pw", Set.of(RoleType.DEVELOPER), null, null, null);

        //When
        adminAccountRepository.save(adminAccount);

        //Then
        assertThat(adminAccountRepository.count()).isEqualTo(previousCount + 1);

    }


    @DisplayName("회원 정보 update 테스트")
    @Test
    void givenAdminAccountAndRoleType_whenUpdating_thenWorksFine() {
        //Given
        AdminAccount adminAccount = adminAccountRepository.getReferenceById("hsm");
        adminAccount.addRoleType(RoleType.DEVELOPER);
        adminAccount.addRoleTypes(List.of(RoleType.USER, RoleType.USER));
        adminAccount.removeRoleType(RoleType.ADMIN);

        //When
        AdminAccount updateAccount = adminAccountRepository.saveAndFlush(adminAccount);

        //Then
        assertThat(updateAccount)
                .hasFieldOrPropertyWithValue("userId", "hsm")
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.DEVELOPER, RoleType.USER));
    }


    @DisplayName("회원 정보 delete 테스트")
    @Test
    void givenAdminAccount_whenDeleting_thenWorksFine() {
        //Given
        long previousCount = adminAccountRepository.count();

        //When
        adminAccountRepository.deleteById("hsm");

        //Then
        assertThat(adminAccountRepository.count()).isEqualTo(previousCount - 1);
    }

    @EnableJpaAuditing
    @TestConfiguration
    public static class TestJpaConfig {
        @Bean
        public AuditorAware<String> auditorAware() {
            return () -> Optional.of("hsm");
        }
    }
}
