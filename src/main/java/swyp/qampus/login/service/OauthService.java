package swyp.qampus.login.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import swyp.qampus.login.entity.User;

public interface OauthService {
    User oAuthLogin(String code, HttpServletResponse httpServletResponse);

    void kakaoLogout(String accessToken) throws JsonProcessingException;
}
