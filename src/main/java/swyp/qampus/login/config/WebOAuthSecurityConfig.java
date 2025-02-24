package swyp.qampus.login.config;

import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.login.util.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// RestTemplate 설정을 위한 Spring Configuration 클래스
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebOAuthSecurityConfig {

    private final JWTUtil jwtUtil;
    private final JWTFilter jwtFilter;

    /**
     * 정적 자원(css, js, img 등)에 대한 보안 설정 비활성화
     * - 해당 경로의 요청은 Spring Security 필터를 거치지 않음
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/img/**", "/css/**", "/js/**" );
    }

    /**
     * 비밀번호 암호화를 위한 PasswordEncoder 빈 등록
     * - BCrypt 해싱 알고리즘 사용
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Spring Security 필터 체인 설정
     *
     * @param http HttpSecurity 객체 (보안 설정 담당)
     * @return SecurityFilterChain 객체
     * @throws Exception 설정 중 예외 발생 가능
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보안 기능 비활성화 (JWT 사용 시 비활성화 권장)
                .csrf(csrf -> csrf.disable())
                // 세션을 사용하지 않고, **Stateless(무상태) 인증 방식**으로 설정 (JWT 방식)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 요청별 접근 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/**","/auth/**", "/oauth2/**","**").permitAll() // 해당 URL은 인증 없이 접근 가능
                        .anyRequest().authenticated() // 그 외의 요청은 인증 필요
                )

                // OAuth2 로그인 기능 활성화 (기본 설정 사용)
                .oauth2Login(Customizer.withDefaults());

        // JWT 필터를 UsernamePasswordAuthenticationFilter 이전에 추가하여 JWT 인증을 적용
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // 보안 설정 완료 후 반환
        return http.build();
    }
}
