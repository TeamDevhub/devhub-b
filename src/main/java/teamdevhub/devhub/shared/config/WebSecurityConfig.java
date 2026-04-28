package teamdevhub.devhub.shared.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import teamdevhub.devhub.outbound.security.auth.CustomAuthenticationProvider;
import teamdevhub.devhub.outbound.security.filter.CustomFilterExceptionHandler;
import teamdevhub.devhub.outbound.security.filter.JwtAuthorizationFilter;
import teamdevhub.devhub.outbound.security.handler.CustomAccessDeniedHandler;
import teamdevhub.devhub.outbound.security.handler.CustomAuthenticationEntryPoint;
import teamdevhub.devhub.core.auth.port.out.token.TokenParseProvider;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final TokenParseProvider tokenParseProvider;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomFilterExceptionHandler customFilterExceptionHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(tokenParseProvider, customFilterExceptionHandler);
    }

    @Bean
    public FilterRegistrationBean<JwtAuthorizationFilter> jwtAuthorizationFilterRegistration(JwtAuthorizationFilter jwtAuthorizationFilter) {
        FilterRegistrationBean<JwtAuthorizationFilter> registration = new FilterRegistrationBean<>(jwtAuthorizationFilter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        config.setAllowedMethods(List.of("HEAD","POST","GET","DELETE","PUT","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.addExposedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   JwtAuthorizationFilter jwtAuthorizationFilter) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .sessionManagement(
                        (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(
                        ex -> ex.accessDeniedHandler(customAccessDeniedHandler)
                                .authenticationEntryPoint(customAuthenticationEntryPoint))
                .authenticationProvider(customAuthenticationProvider)
                .authorizeHttpRequests(
                        (authorizeHttpRequests) -> authorizeHttpRequests
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-resources/**",
                                        "/v3/api-docs/**",
                                        "/webjars/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/user/signup").permitAll()
                                .requestMatchers("/common/**").permitAll()
                                .requestMatchers("/files/**").permitAll()
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/terms/**").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated())

                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}