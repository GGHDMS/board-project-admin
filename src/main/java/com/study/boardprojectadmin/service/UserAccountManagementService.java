package com.study.boardprojectadmin.service;

import com.study.boardprojectadmin.dto.UserAccountDto;
import com.study.boardprojectadmin.dto.properties.ProjectProperties;
import com.study.boardprojectadmin.dto.response.UserAccountClientResponse;
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
public class UserAccountManagementService {

    private final RestTemplate restTemplate;

    private final ProjectProperties projectProperties;

    public List<UserAccountDto> getUserAccounts() {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/userAccounts")
                .queryParam("size", 10000) //TODO: 모든 테이터를 가져오기 위해 단순히 큰 수를 사용해서 불안한 로직이다.
                .build()
                .toUri();

        UserAccountClientResponse response = restTemplate.getForObject(uri, UserAccountClientResponse.class);
        return Optional.ofNullable(response).orElseGet(UserAccountClientResponse::empty).getEmbedded().getUserAccounts();
    }

    public UserAccountDto getUserAccount(String userId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/userAccounts/" + userId)
                .build()
                .toUri();

        UserAccountDto response = restTemplate.getForObject(uri, UserAccountDto.class);
        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("유저가 없습니다 - userAccountId: " + userId));
    }

    public void deleteUserAccount(String userId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.getBoard().getUrl() + "/api/userAccounts/" + userId)
                .build()
                .toUri();

        restTemplate.delete(uri);
    }
}
