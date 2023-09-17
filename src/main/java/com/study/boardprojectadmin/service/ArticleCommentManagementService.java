package com.study.boardprojectadmin.service;

import com.study.boardprojectadmin.dto.ArticleCommentDto;
import com.study.boardprojectadmin.dto.properties.ProjectProperties;
import com.study.boardprojectadmin.dto.response.ArticleCommentClientResponse;
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
public class ArticleCommentManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties projectProperties;

    public List<ArticleCommentDto> getArticleComments() {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/articleComments")
                .queryParam("size", 10000) //TODO: 모든 테이터를 가져오기 위해 단순히 큰 수를 사용해서 불안한 로직이다.
                .build()
                .toUri();

        ArticleCommentClientResponse response = restTemplate.getForObject(uri, ArticleCommentClientResponse.class);
        return Optional.ofNullable(response).orElseGet(ArticleCommentClientResponse::empty).articleComments();
    }

    public ArticleCommentDto getArticleComment(Long articleCommentId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/articleComments/" + articleCommentId)
                .queryParam("projection", "withUserAccount")
                .build()
                .toUri();

        ArticleCommentDto response = restTemplate.getForObject(uri, ArticleCommentDto.class);
        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("댓글이 없습니다 - articleCommentId: " + articleCommentId));
    }

    public void deleteArticleComment(Long articleCommentId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/articleComments/" + articleCommentId)
                .build()
                .toUri();

        restTemplate.delete(uri);
    }
}
