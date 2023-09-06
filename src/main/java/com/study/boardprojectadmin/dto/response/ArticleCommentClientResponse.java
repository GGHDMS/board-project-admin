package com.study.boardprojectadmin.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.boardprojectadmin.dto.ArticleCommentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCommentClientResponse {
    @JsonProperty("_embedded")
    Embedded embedded;
    @JsonProperty("page")
    Page page;


    public static ArticleCommentClientResponse empty() {
        return new ArticleCommentClientResponse(
                new Embedded(List.of()),
                new Page(1, 0, 1, 0)
        );
    }

    public static ArticleCommentClientResponse of(List<ArticleCommentDto> articleComments) {
        return new ArticleCommentClientResponse(
                new Embedded(articleComments),
                new Page(articleComments.size(), articleComments.size(), 1, 0)
        );
    }

    public List<ArticleCommentDto> articleComments() {
        return this.embedded.getArticleComments();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Embedded{
        @JsonProperty("articleComments")
        List<ArticleCommentDto> articleComments;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Page{
        int size;
        long totalElements;
        int totalPages;
        int number;
    }
}
