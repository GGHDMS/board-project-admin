package com.study.boardprojectadmin.dto.security;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
@Builder
@SuppressWarnings("unchecked") // 미확인 오퍼레이션과 관련된 경고를 억제한다 TODO: Map -> Object 변환 로직이 있어 제네릭 타입 캐스팅 문제를 무시한다. 더 좋은 방법이 있다면 고려할 수 있음.
public class KakaoOAuth2Response {
    private Long id;
    private LocalDateTime connectedAt;
    private Map<String, Object> properties;
    private KakaoAccount kakaoAccount;

    @Data
    @Builder
    public static class KakaoAccount {
        private Boolean profileNicknameNeedsAgreement;
        private Profile profile;
        private Boolean hasEmail;
        private Boolean emailNeedsAgreement;
        private Boolean isEmailValid;
        private Boolean isEmailVerified;
        private String email;

        @Data
        @Builder
        public static class Profile {
            private String nickname;

            public static Profile from(Map<String, Object> attributes) {
                return Profile.builder()
                        .nickname(String.valueOf(attributes.get("nickname")))
                        .build();
            }
        }

        public static KakaoAccount from(Map<String, Object> attributes) {
            return KakaoAccount.builder()
                    .profileNicknameNeedsAgreement(Boolean.valueOf(String.valueOf(attributes.get("profile_nickname_needs_agreement")))) //value.of 은 null 일 때 "null"을 반환한다.
                    .profile(Profile.from((Map<String, Object>) attributes.get("profile")))
                    .hasEmail(Boolean.valueOf(String.valueOf(attributes.get("has_email"))))
                    .emailNeedsAgreement(Boolean.valueOf(String.valueOf(attributes.get("email_needs_agreement"))))
                    .isEmailValid(Boolean.valueOf(String.valueOf(attributes.get("is_email_valid"))))
                    .isEmailVerified(Boolean.valueOf(String.valueOf(attributes.get("is_email_verified"))))
                    .email(String.valueOf(attributes.get("email")))
                    .build();
        }

        public String nickname() { return this.getProfile().getNickname(); }
    }

    public static KakaoOAuth2Response from(Map<String, Object> attributes) {
        return KakaoOAuth2Response.builder()
                .id(Long.valueOf(String.valueOf(attributes.get("id"))))
                .connectedAt(LocalDateTime.parse(
                        String.valueOf(attributes.get("connected_at")),
                        DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())
                ))
                .properties((Map<String, Object>) attributes.get("properties"))
                .kakaoAccount(KakaoAccount.from((Map<String, Object>) attributes.get("kakao_account")))
                .build();
    }

    public String email() { return this.getKakaoAccount().getEmail(); }
    public String nickname() { return this.getKakaoAccount().nickname(); }
}