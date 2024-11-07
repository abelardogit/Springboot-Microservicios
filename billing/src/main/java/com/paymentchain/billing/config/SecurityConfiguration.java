package com.paymentchain.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    String[] NO_AUTH_LIST = {
            "/v3/api-docs/**",
            "/configuration/ui/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/login/**",
            "/h2-console/**",
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests((requests) -> requests
                .anyRequest().authenticated()
            )
//            .formLogin((form) -> form
//                    .loginPage("/login")
//                    .permitAll()
//            )
            .logout(LogoutConfigurer::permitAll)
        ;
//        return http.build();

//        http
//            .csrf().disable()
//            .authorizeHttpRequests((authz) -> authz
//                    .requestMatchers(HttpMethod.POST, "/billing*/**").authenticated()
//                    .requestMatchers(HttpMethod.GET, "/billing*/**").hasAnyRole("ADMIN")
//                    .requestMatchers(NO_AUTH_LIST).permitAll()
//                    .anyRequest().authenticated()
//            )
//            .formLogin((form) -> form
//                    .loginPage("/login")
//                    .permitAll()
//            )
//            .logout(LogoutConfigurer::permitAll)
//        ;

        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedHeaders(Arrays.asList("Origin,Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Authorization"));
        configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        configuration.setAllowedOrigins(List.of("/*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "PATCH"));
        configuration.addAllowedOriginPattern("*");
        configuration.setMaxAge(Duration.ZERO);
        configuration.setAllowCredentials(Boolean.TRUE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
