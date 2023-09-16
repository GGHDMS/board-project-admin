package com.study.boardprojectadmin.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.study.boardprojectadmin.dto.ArticleCommentDto;
import com.study.boardprojectadmin.dto.UserAccountDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleCommentResponse {
    Long id;
    UserAccountDto userAccount;
    String content;
    LocalDateTime createdAt;

    private ArticleCommentResponse(Long id, UserAccountDto userAccount, String content, LocalDateTime createdAt) {
        this.id = id;
        this.userAccount = userAccount;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static ArticleCommentResponse of(Long id, UserAccountDto userAccount, String content, LocalDateTime createdAt) {
        return new ArticleCommentResponse(id, userAccount, content, createdAt);
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto) {
        return new ArticleCommentResponse(dto.getId(), dto.getUserAccount(), dto.getContent(), dto.getCreatedAt());
    }


}
