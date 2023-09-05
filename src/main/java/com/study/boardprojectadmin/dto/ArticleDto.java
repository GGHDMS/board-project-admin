package com.study.boardprojectadmin.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ArticleDto {
    Long id;
    UserAccountDto userAccount;
    String title;
    String content;
    Set<String> hashtags;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime modifiedAt;
    String modifiedBy;

    private ArticleDto(Long id, UserAccountDto userAccount, String title, String content, Set<String> hashtags, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        this.id = id;
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtags = hashtags;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }

    public static ArticleDto of(Long id, UserAccountDto userAccountDto, String title, String content, Set<String> hashtags, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleDto(id, userAccountDto, title, content, hashtags, createdAt, createdBy, modifiedAt, modifiedBy);
    }
}
