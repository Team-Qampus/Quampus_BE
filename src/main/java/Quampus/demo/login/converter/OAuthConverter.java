package Quampus.demo.login.converter;

import Quampus.demo.login.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class OAuthConverter {

    public static User toUser(String email, String name, String password, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .build();
    }
}
