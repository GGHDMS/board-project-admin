package com.study.boardprojectadmin.service;

import com.study.boardprojectadmin.domain.AdminAccount;
import com.study.boardprojectadmin.domain.constant.RoleType;
import com.study.boardprojectadmin.dto.AdminAccountDto;
import com.study.boardprojectadmin.repository.AdminAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminAccountService {

    private final AdminAccountRepository adminAccountRepository;

    @Transactional(readOnly = true)
    public Optional<AdminAccountDto> searchUser(String userId) {
        return adminAccountRepository.findById(userId)
                .map(AdminAccountDto::from);
    }

    public AdminAccountDto saveUser(String username, String password, Set<RoleType> roleTypes, String email, String memo, String nickname) {
        return AdminAccountDto.from(
                adminAccountRepository.save(AdminAccount.of(username, password, roleTypes, email, nickname, memo))
        );
    }

    @Transactional(readOnly = true)
    public List<AdminAccountDto> users() {
        return adminAccountRepository.findAll().stream()
                .map(AdminAccountDto::from)
                .collect(Collectors.toList());
    }

    public void deleteUser(String userId) {
        adminAccountRepository.deleteById(userId);
    }
}
