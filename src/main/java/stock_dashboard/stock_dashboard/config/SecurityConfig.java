//package stock_dashboard.stock_dashboard.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AndRequestMatcher;
//import org.springframework.util.AntPathMatcher;
//import stock_dashboard.stock_dashboard.Filter.JwtFilter;
////import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtFilter jwtFilter;
//
//    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http
////                .csrf(csrf -> csrf.disable())
////                .sessionManagement(session ->
////                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .authorizeHttpRequests(auth -> auth
////
////                        // PUBLIC - no token needed
////                        .requestMatchers(
////                                new AntPathMatcher("/api/auth/**"),
////                                new AntPathMatcher("/api/stocks/**"),
////                                new AntPathMatcher("/api/recommendation/**"),
////                                new AntPathMatcher("/h2-console/**")                //H2 browser console
////                        ).permitAll()
////                        .anyRequest().authenticated()
////
////                        //PROTECTED - requires JWT
////                        .requestMatchers("/api/portfolio/**").authenticated()
////
////                        // everything else also requires auth
////                        .anyRequest().authenticated()
////                )
////                .headers(headers ->
////                        headers.frameOptions(f -> f.disable())) //needed for H2 console
////                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
////
////        return http.build();
////    }
//
//
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                new AntPathMatcher("/api/auth/**"),
//                                new AntPathMatcher("/api/stocks/**"),
//                                new AntPathMatcher("/api/recommendation/**"),
//                                new AntPathMatcher("/h2-console/**")
//                        )
//                        .permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                // Fix for H2 console iframe — must use this exact syntax
//                .headers(headers ->
//                        headers.frameOptions(AbstractHttpConfigurer::disable))
//
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//}



//package com.stocks.config;

package stock_dashboard.stock_dashboard.config;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import stock_dashboard.stock_dashboard.Filter.JwtFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/stocks/**",
                                "/api/recommendation/**",
                                "/h2-console/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers ->
                                headers.frameOptions(frameOptions -> frameOptions.disable()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}