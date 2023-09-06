package com.study.boardprojectadmin.dto;

import com.study.boardprojectadmin.domain.constant.RoleType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserAccountDto {
    String userId;
    Set<RoleType> roleTypes;
    String email;
    String nickname;
    String memo;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime modifiedAt;
    String modifiedBy;

    private UserAccountDto(String userId, Set<RoleType> roleTypes, String email, String nickname, String memo, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        this.userId = userId;
        this.roleTypes = roleTypes;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }

    public static UserAccountDto of(String userId, Set<RoleType> roleTypes, String email, String nickname, String memo) {
        return UserAccountDto.of(userId, roleTypes, email, nickname, memo, null, null, null, null);
    }

    public static UserAccountDto of(String userId, Set<RoleType> roleTypes, String email, String nickname, String memo, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new UserAccountDto(userId, roleTypes, email, nickname, memo, createdAt, createdBy, modifiedAt, modifiedBy);
    }
}
