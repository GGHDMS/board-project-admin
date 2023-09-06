package com.study.boardprojectadmin.repository;

import com.study.boardprojectadmin.domain.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminAccountRepository extends JpaRepository<AdminAccount, String> {
}
