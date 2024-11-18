package pl.jania1857.fms.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.jania1857.fms.security.JwtAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/v1")
                .authorizeHttpRequests(req -> req
                        .requestMatchers(
                                "/v2/api/api-docs",
                                "/v2/api/api-docs/**",
                                "/v3/api/api-docs",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/v3/api/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                /////////// PUBLIC ENDPOINTS /////////
                                "/api/v1/user/login",
                                "/api/v1/user/change-password"
                        ).permitAll()

                        .requestMatchers(
                        "/api/v1/**",
                        "/api/v1/user/register"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/api/v1/manager/**"
                        ).hasRole("MANAGER")

                        .requestMatchers(
                                "/api/v1/driver/**"
                        ).hasRole("DRIVER")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
