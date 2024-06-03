package com.mysite.bookp4.config;

import com.mysite.bookp4.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    @SuppressWarnings("unused")
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authz) ->
                        authz
                                .requestMatchers("/js/**", "/css/**").permitAll()
                                .requestMatchers("/login","/register","/registUser").permitAll()//여기만 열음
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .anyRequest().authenticated()//전체 허용.permitAll() , 제외한 주소 인증받기(로그인 폼 나옴)리퀘스트 매처의 주소들은 허가하고 그 외의 주소는 인증 요구함
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .successHandler(customAuthenticationSuccessHandler) // 커스텀 성공 핸들러 적용
                        .usernameParameter("email")
                        .passwordParameter("password")
                )
                .logout((logout)->logout.logoutUrl("/logout").invalidateHttpSession(true).clearAuthentication(true)
                        .logoutSuccessUrl("/login?logout=true").permitAll())
        ; // 인증서 로그인 페이지를 지정하고 실패시 주소 지정 성공주소 지정
        return http.build();
    }


    //정적파일 예외로 처리(시큐리티 예외)
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->
                web.ignoring().requestMatchers("/css/**", "/js/**", "/error");

    }
    //시큐리티 인증 객체 인증담당
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //암호화 객체
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}