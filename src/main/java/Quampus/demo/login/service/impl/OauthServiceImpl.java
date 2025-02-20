package Quampus.demo.login.service.impl;

import Quampus.demo.login.converter.OAuthConverter;
import Quampus.demo.login.dto.KakaoDTO;
import Quampus.demo.login.dto.KakaoUtil;
import Quampus.demo.login.entity.User;
import Quampus.demo.login.repository.UserRepository;
import Quampus.demo.login.service.OauthService;
import Quampus.demo.login.util.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class OauthServiceImpl implements OauthService {

    private final KakaoUtil kakaoUtil;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User oAuthLogin(String code, HttpServletResponse httpServletResponse) {

        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(code);
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);
        Optional<User> queryUser = userRepository.findByEmail(kakaoProfile.getKakao_account().getEmail());

        if (queryUser.isPresent()) {
            User user = queryUser.get();
            httpServletResponse.setHeader("Authorization", jwtUtil.createAccessToken(user.getEmail()));
            return user;
        } else {
            User user = OAuthConverter.toUser(kakaoProfile.getKakao_account().getEmail(),
                    kakaoProfile.getKakao_account().getProfile().getNickname(),
                    "1234",
                    passwordEncoder);
            userRepository.save(user);
            httpServletResponse.setHeader("Authorization", jwtUtil.createAccessToken(user.getEmail()));
            return user;
        }
    }
}
