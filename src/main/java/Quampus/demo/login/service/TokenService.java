package Quampus.demo.login.service;

import Quampus.demo.login.entity.User;
import Quampus.demo.login.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    // 리프레쉬 토큰으로 토큰 유효성검사를 하고, 유효한 토큰일 때 리프레쉬 토큰으로 사용자 ID를 찾는다.
    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected toekn");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
