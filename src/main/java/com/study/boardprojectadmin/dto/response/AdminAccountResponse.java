package com.study.boardprojectadmin.dto.response;

import com.study.boardprojectadmin.domain.constant.RoleType;
import com.study.boardprojectadmin.dto.AdminAccountDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Data
public class AdminAccountResponse {
    private String userId;
    private String roleTypes;
    private String email;
    private String nickname;
    private String memo;
    private LocalDateTime createdAt;
    private String createdBy;

    public AdminAccountResponse(String userId, String roleTypes, String email, String nickname, String memo, LocalDateTime createdAt, String createdBy) {
        this.userId = userId;
        this.roleTypes = roleTypes;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public static AdminAccountResponse of(String userId, String roleTypes, String email, String nickname, String memo, LocalDateTime createdAt, String createdBy) {
        return new AdminAccountResponse(userId, roleTypes, email, nickname, memo, createdAt, createdBy);
    }

    public static AdminAccountResponse from(AdminAccountDto dto) {
        return AdminAccountResponse.of(
                dto.getUserId(),
                dto.getRoleTypes().stream()
                        .map(RoleType::getDescription)
                        .collect(Collectors.joining(", ")),
                dto.getEmail(),
                dto.getNickname(),
                dto.getMemo(),
                dto.getCreatedAt(),
                dto.getCreatedBy()
        );
    }


}
