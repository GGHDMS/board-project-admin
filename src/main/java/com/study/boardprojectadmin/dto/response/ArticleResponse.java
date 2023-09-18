package com.study.boardprojectadmin.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.study.boardprojectadmin.dto.ArticleDto;
import com.study.boardprojectadmin.dto.UserAccountDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleResponse {
    private Long id;
    private UserAccountDto userAccount;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    private ArticleResponse(Long id, UserAccountDto userAccount, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static ArticleResponse of(Long id, UserAccountDto userAccount, String title, String content, LocalDateTime createdAt) {
        return new ArticleResponse(id, userAccount, title, content, createdAt);
    }

    public static ArticleResponse withContent(ArticleDto dto) {
        return new ArticleResponse(dto.getId(), dto.getUserAccount(), dto.getTitle(), dto.getContent(), dto.getCreatedAt());
    }

    public static ArticleResponse withoutContent(ArticleDto dto) {
        return new ArticleResponse(dto.getId(), dto.getUserAccount(), dto.getTitle(), null, dto.getCreatedAt());
    }

}
