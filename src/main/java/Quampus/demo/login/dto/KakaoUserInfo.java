package Quampus.demo.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KakaoUserInfo {
    private Long id;
    private String nickname;
    private String profileImageUrl;
    private String email;
}
