package CoinIndicator.CoinIndicator.config;

import CoinIndicator.CoinIndicator.user.entity.Role;
import CoinIndicator.CoinIndicator.user.service.AuthService;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(List.of("GET", "POST", "DELETE"));
//            config.setAllowedOriginPatterns(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(List.of("http://coinindicator.site", "https://coinindicator.site", "http://localhost:8080", "http://localhost:3000"));
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);
            return config;
        };
    }

    @Bean
    public HttpSessionListener sessionListener() {
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {
                se.getSession().setMaxInactiveInterval(240 * 60); // 240분 (240 * 60초)
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                // 세션이 파괴될 때의 처리;
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement
                                .sessionFixation().migrateSession()
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .maximumSessions(4)
                                .expiredUrl("/"))
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/v1/feedback**").authenticated()
                                .requestMatchers("/v1/**manage**").hasAuthority(Role.ADMIN.getKey())
                                .anyRequest().permitAll());

//                .oauth2Login(oauth2 ->
//                        oauth2.defaultSuccessUrl("/")
//                                .failureUrl("/login?error=true"))
//                .logout(logout ->
//                        logout.logoutSuccessUrl("/"))
//                ;

        return http.build();
    }
}
