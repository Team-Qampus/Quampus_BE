package swyp.qampus.login.service;


import swyp.qampus.login.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;

public interface OauthService {
    User oAuthLogin(String code, HttpServletResponse httpServletResponse);

    void kakaoLogout(String accessToken) throws JsonProcessingException;
}
