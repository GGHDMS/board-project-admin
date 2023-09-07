package com.study.boardprojectadmin.service;

import com.study.boardprojectadmin.domain.AdminAccount;
import com.study.boardprojectadmin.domain.constant.RoleType;
import com.study.boardprojectadmin.dto.AdminAccountDto;
import com.study.boardprojectadmin.repository.AdminAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 어드민 회원")
@ExtendWith(MockitoExtension.class)
class AdminAccountServiceTest {

    @InjectMocks
    private AdminAccountService sut;

    @Mock
    private AdminAccountRepository adminAccountRepository;

    @Test
    @DisplayName("존재하는 회원 ID를 입력하면 회원 데이터를 Optional 로 반환한다.")
    public void givenUserId_whenSearching_thenReturnOptionalUserData() throws Exception{
        //given
        String userId = "hsm";
        given(adminAccountRepository.findById(userId)).willReturn(Optional.of(createAdminAccount(userId)));

        //when
        Optional<AdminAccountDto> result = sut.searchUser(userId);

        //then
        assertThat(result).isPresent();
        then(adminAccountRepository).should().findById(userId);
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID를 입력하면 비어있는 Optional 로 반환한다.")
    public void givenNonExistentUserId_whenSearching_thenReturnOptionalOfEmpty() throws Exception{
        //given
        String userId = "wrong-user";
        given(adminAccountRepository.findById(userId)).willReturn(Optional.empty());

        //when
        Optional<AdminAccountDto> result = sut.searchUser(userId);

        //then
        assertThat(result).isEmpty();
        then(adminAccountRepository).should().findById(userId);
    }

    @Test
    @DisplayName("회원 정보를 입력하면, 새로운 회원 정보를 저장하고 가입시킨다.")
    public void givenUserParams_whenSaving_thenSavesAdminAccount() throws Exception{
        //given
        AdminAccount adminAccount = createSigningUpAdminAccount("hsm", Set.of(RoleType.USER));

        //when
        AdminAccountDto result = sut.saveUser(adminAccount.getUserId(), adminAccount.getUserPassword(), adminAccount.getRoleTypes(), adminAccount.getEmail(), adminAccount.getNickname(), adminAccount.getMemo());

        //then
        assertThat(result)
                .hasFieldOrPropertyWithValue("userId", adminAccount.getUserId())
                .hasFieldOrPropertyWithValue("userPassword", adminAccount.getUserPassword())
                .hasFieldOrPropertyWithValue("roleTypes", adminAccount.getRoleTypes())
                .hasFieldOrPropertyWithValue("email", adminAccount.getEmail())
                .hasFieldOrPropertyWithValue("nickname", adminAccount.getNickname())
                .hasFieldOrPropertyWithValue("memo", adminAccount.getMemo())
                .hasFieldOrPropertyWithValue("createBy", adminAccount.getCreatedBy())
                .hasFieldOrPropertyWithValue("modifiedBy", adminAccount.getModifiedBy());

        then(adminAccountRepository).should().save(adminAccount);
    }

    @Test
    @DisplayName("전체 어드민 회원을 조회한다.")
    public void givenNothing_whenSelectingAdminAccounts_thenReturnAdminAccountList() throws Exception{
        //given
        given(adminAccountRepository.findAll()).willReturn(List.of());

        //when
        List<AdminAccountDto> users = sut.users();

        //then
        assertThat(users).hasSize(0);
        then(adminAccountRepository).should().findAll();
    }
    
    @Test
    @DisplayName("회원 ID를 입력하면, 회원을 삭제한다")
    public void givenUserId_whenDeleting_thenDeleteAdminAccount() throws Exception{
        //given
        String userId = "hsm";
        willDoNothing().given(adminAccountRepository).deleteById(userId);
        
        //when
        sut.deleteUser(userId);
        
        //then
        then(adminAccountRepository).should().deleteById(userId);
    }
    
    @Test
    @DisplayName("회원 ID를 입력하면, 회원을 삭제한다")
    public void given_when_then() throws Exception{
        //given
        String userId = "hsm";
        willDoNothing().given(adminAccountRepository).deleteById(userId);
        
        //when
        sut.deleteUser(userId);
        
        //then
        then(adminAccountRepository).should().deleteById(userId);
    }
    

    private AdminAccount createAdminAccount(String username) {
        return createAdminAccount(username, Set.of(RoleType.USER), null);
    }

    private AdminAccount createSigningUpAdminAccount(String username, Set<RoleType> roleTypes) {
        return createAdminAccount(username, roleTypes, username);
    }

    private AdminAccount createAdminAccount(String username, Set<RoleType> roleTypes, String createdBy) {
        return AdminAccount.of(
                username,
                "password",
                roleTypes,
                "hsm@mail.com",
                "nickname",
                "memo",
                createdBy
        );
    }

}
