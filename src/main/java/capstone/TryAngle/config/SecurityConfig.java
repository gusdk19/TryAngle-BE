package capstone.TryAngle.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider; // JWT 생성 및 검증
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 인증 실패 시 처리
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler; // 인가 실패 시 처리
    private final WebMvcConfigurer corsConfigurer;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring Security 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.disable())
                // CSRF 보호 기능 비활성화
                .csrf(csrf -> csrf.disable())
                // 세션 사용하지 않으므로 stateless
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 예외 처리 핸들러
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                // HttpServletRequest를 사용하는 요청들에 대해 접근제한 설정
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 가능한 경우
                        .requestMatchers("/user/login", "/user/signup", "/user/checkEmail", "/user/checkNickname").permitAll()
//                        .requestMatchers("/user/*", "/challenge/*").permitAll() // 임시적으로 모두 허용
                        .anyRequest().authenticated()) // 그 외에는 인증 없이 접근 불가
                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 이전에 등록
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
