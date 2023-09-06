package com.study.boardprojectadmin.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.boardprojectadmin.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountClientResponse {
    @JsonProperty("_embedded")
    Embedded embedded;
    @JsonProperty("page")
    Page page;


    public static UserAccountClientResponse empty() {
        return new UserAccountClientResponse(
                new Embedded(List.of()),
                new Page(1, 0, 1, 0)
        );
    }

    public static UserAccountClientResponse of(List<UserAccountDto> userAccounts) {
        return new UserAccountClientResponse(
                new Embedded(userAccounts),
                new Page(userAccounts.size(), userAccounts.size(), 1, 0)
        );
    }

    public List<UserAccountDto> userAccounts() {
        return this.embedded.getUserAccounts();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Embedded {
        @JsonProperty("userAccounts")
        List<UserAccountDto> userAccounts;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Page {
        int size;
        long totalElements;
        int totalPages;
        int number;
    }
}
