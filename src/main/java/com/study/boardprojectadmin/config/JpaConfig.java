package com.study.boardprojectadmin.config;

import com.study.boardprojectadmin.dto.security.BoardAdminPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext()) //security 에 대한 모든 정보로부터 getContext()
                .map(SecurityContext::getAuthentication) //getContext() -> SecurityContext
                .filter(Authentication::isAuthenticated) //인증이 되었는지 검사
                .map(Authentication::getPrincipal) // 어떤 종류의 인증 정보 인지 모르는 인증 정보 들어 있음  -> 여기선 UserDetails 구현체
                .map(BoardAdminPrincipal.class::cast) // UserDetails 구현체 -> BoardPrincipal
                .map(BoardAdminPrincipal::getUsername);
    }
}
