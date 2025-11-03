package nl.fontys.s3.gamerpc.business.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;

@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@Configuration
public class WebSecurityConfig {
    private static final String ADMIN_ROLE = "ADMIN";
    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           AuthenticationEntryPoint authenticationEntryPoint,
                                           AuthenticationRequestFilter authenticationRequestFilter) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session  ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.GET, "/benchmark/all", "/cpu", "/gpu", "/ram").permitAll()
                                .requestMatchers(HttpMethod.POST, "/login", "/benchmark/save","/user").permitAll()
                                .requestMatchers(HttpMethod.POST, "/cpu","/gpu","/ram").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/cpu","/gpu","/ram").hasAnyRole(ADMIN_ROLE)
                                .requestMatchers(HttpMethod.PUT, "/cpu", "/gpu", "/ram").hasAnyRole(ADMIN_ROLE)
                                .anyRequest().authenticated()
                )
                .exceptionHandling(configure ->
                        configure
                                .authenticationEntryPoint((request, response, authException) -> {
                                    logger.warn("Unauthorized access attempt: {}", request.getRequestURI());
                                    authenticationEntryPoint.commence(request, response, authException);
                                }))
                .addFilterBefore(authenticationRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                        .allowedHeaders("Authorization","Content-Type","Authentication");
            }
        };
    }
}
