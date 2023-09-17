package com.study.boardprojectadmin.dto.response;

import com.study.boardprojectadmin.dto.UserAccountDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAccountResponse {
    private String userId;
    private String email;
    private String nickname;
    private String memo;
    private LocalDateTime createdAt;
    private String createdBy;


    private UserAccountResponse(String userId, String email, String nickname, String memo, LocalDateTime createdAt,
                                String createdBy) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public static UserAccountResponse of(String userId, String email, String nickname, String memo, LocalDateTime createdAt, String createdBy) {
        return new UserAccountResponse(userId, email, nickname, memo, createdAt, createdBy);
    }

    public static UserAccountResponse from(UserAccountDto dto) {
        return new UserAccountResponse(
                dto.getUserId(),
                dto.getEmail(),
                dto.getNickname(),
                dto.getMemo(),
                dto.getCreatedAt(),
                dto.getCreatedBy()
        );
    }


}
