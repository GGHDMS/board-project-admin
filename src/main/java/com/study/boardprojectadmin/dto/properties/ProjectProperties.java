package com.study.boardprojectadmin.dto.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 어드민 프로젝트 전용 프로퍼티
 */
@Data
@ConfigurationProperties("project")
public class ProjectProperties {
    private Board board;

    /**
     * 게시판 관련 프로퍼티
     */
    @Data
    public static class Board {
        private String url;
    }
}
