package Quampus.demo.login.controller;

import Quampus.demo.login.converter.UserConverter;
import Quampus.demo.login.dto.BaseResponse;
import Quampus.demo.login.dto.UserResponseDTO;
import Quampus.demo.login.entity.User;
import Quampus.demo.login.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OauthService oauthService;

    @GetMapping("/auth/login/kakao")
    public ResponseEntity<UserResponseDTO.JoinResultDTO> kakaoLogin(@RequestParam("code") String code, HttpServletResponse httpServletResponse) {
        User user = oauthService.oAuthLogin(code, httpServletResponse);
        return ResponseEntity.ok(UserConverter.toJoinResultDTO(user));
    }
}
