package com.study.boardprojectadmin.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleCommentDto {
    Long id;
    Long articleId;
    UserAccountDto userAccount;
    Long parentCommentId;
    String content;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime modifiedAt;
    String modifiedBy;

    private ArticleCommentDto(Long id, Long articleId, UserAccountDto userAccount, Long parentCommentId, String content, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        this.id = id;
        this.articleId = articleId;
        this.userAccount = userAccount;
        this.parentCommentId = parentCommentId;
        this.content = content;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }

    public static ArticleCommentDto of(Long id, Long articleId, UserAccountDto userAccount, Long parentCommentId, String content, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleCommentDto(id, articleId, userAccount, parentCommentId, content, createdAt, createdBy, modifiedAt, modifiedBy);
    }
}
