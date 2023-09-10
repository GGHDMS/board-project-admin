package com.study.boardprojectadmin.service;

import com.study.boardprojectadmin.dto.ArticleDto;
import com.study.boardprojectadmin.dto.properties.ProjectProperties;
import com.study.boardprojectadmin.dto.response.ArticleClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties projectProperties;

    public List<ArticleDto> getArticles() {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/articles")
                .queryParam("size", 10000) //TODO: 모든 테이터를 가져오기 위해 단순히 큰 수를 사용해서 불안한 로직이다.
                .build()
                .toUri();

        ArticleClientResponse response = restTemplate.getForObject(uri, ArticleClientResponse.class);
        return Optional.ofNullable(response).orElseGet(ArticleClientResponse::empty).articles();
    }

    public ArticleDto getArticle(Long articleId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/articles/" + articleId)
                .build()
                .toUri();

        ArticleDto response = restTemplate.getForObject(uri, ArticleDto.class);
        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("게시글이 없습니다 - articleId: " + articleId));
    }

    public void deleteArticle(Long articleId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/articles/" + articleId)
                .build()
                .toUri();

        restTemplate.delete(uri);
    }
}
