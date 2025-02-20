package Quampus.demo.login.service;


import Quampus.demo.login.entity.User;
import jakarta.servlet.http.HttpServletResponse;

public interface OauthService {
    User oAuthLogin(String code, HttpServletResponse httpServletResponse);
}
