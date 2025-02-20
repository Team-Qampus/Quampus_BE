package Quampus.demo.login.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class KakaoDTO {

    @Getter
    public static class OAuthToken {
        private String access_token;
        private String token_type;
        private String refresh_token;
        private int expires_in;
        private String scope;
        private int refresh_token_expires_in;
    }
    @Getter
    public static class KakaoProfile {
        private Long id;
        private String connected_at;
        private Properties properties;
        private KakaoAccount kakao_account;

        @Getter
        public class Properties {
            private String nickname;
        }

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class KakaoAccount {
            @JsonProperty("email")
            private String email;

            @JsonProperty("is_email_valid")
            private Boolean isEmailValid;

            @JsonProperty("is_email_verified")
            private Boolean isEmailVerified;

            @JsonProperty("has_email")
            private Boolean hasEmail;

            @JsonProperty("profile_nickname_needs_agreement")
            private Boolean profileNicknameNeedsAgreement;

            @JsonProperty("email_needs_agreement")
            private Boolean emailNeedsAgreement;

            private Profile profile;

            @Getter
            public class Profile {
                private String nickname;
                private Boolean is_default_nickname;
            }
        }
    }
}
