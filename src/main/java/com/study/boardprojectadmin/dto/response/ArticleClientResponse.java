package com.study.boardprojectadmin.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.boardprojectadmin.dto.ArticleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleClientResponse {
    @JsonProperty("_embedded")
    Embedded embedded;
    @JsonProperty("page")
    Page page;


    public static ArticleClientResponse empty() {
        return new ArticleClientResponse(
                new Embedded(List.of()),
                new Page(1, 0, 1, 0)
        );
    }

    public static ArticleClientResponse of(List<ArticleDto> articles) {
        return new ArticleClientResponse(
                new Embedded(articles),
                new Page(articles.size(), articles.size(), 1, 0)
        );
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Embedded{
        @JsonProperty("articles")
        List<ArticleDto> articles;
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
