package Quampus.demo.login.service;

import Quampus.demo.login.entity.RefreshToken;
import Quampus.demo.login.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // 리프레쉬 토큰 객체를 검색
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Unexpected token" + refreshToken));
    }
}
