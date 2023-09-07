package com.example.myapp.user.mber.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.myapp.user.mber.service.CustomUserDetailsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	

	 @Bean // 패스워드 암호화 관련 메소드 
	 public PasswordEncoder passwordEncoder() { 
	 return new BCryptPasswordEncoder(); 
}
	
	@Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
						
						 .requestMatchers("/css/**", "/img/**", "/mber/signup", 
								 			"/", "/mber/signin", "/mber/signout", "/js/**",
								 			"/user/content", "/cms/**", "/user/**").permitAll() 
						 .requestMatchers(HttpMethod.GET, "/mber/mypage", "/cms/**", "/user/**").hasAnyRole("ADMIN") 
						 .requestMatchers(HttpMethod.GET, "/mber/mypage", "/user/**").hasAnyRole("USER")
						 .anyRequest().authenticated())
					    /*.anyRequest().permitAll())*/
           	 // Form 로그인을 활용하는 경우
			    .formLogin(formLogin -> formLogin
			            .loginPage("/mber/signin")
			            .loginProcessingUrl("/mber/signin")
			            .usernameParameter("mberId")
			            .passwordParameter("mberPwd")
			            .defaultSuccessUrl("/", true)
			            .successHandler(new AuthenticationSuccessHandler() {
							@Override
							public void onAuthenticationSuccess(HttpServletRequest request,
									HttpServletResponse response, Authentication authentication)
									throws IOException, ServletException {
						        response.sendRedirect("/");
							}
			            })
			            .failureHandler(new AuthenticationFailureHandler() {
			                @Override
			                public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
			                    System.out.println("exception: " + exception.getMessage());
			                    response.sendRedirect("/mber/signin");
			                }
			            })
			            .permitAll())
					            
					    .logout(logout -> logout // 로그아웃 기능 작동함
					            .logoutUrl("/mber/signout")
					            .logoutSuccessUrl("/")); // 로그아웃 성공 후 이동페이지
//					            .deleteCookies("JSESSIONID", "remember-me")); // 로그아웃 후 쿠키 삭제
//					            .addLogoutHandler( ...생략... ) // 로그아웃 핸들러
//					            .logoutSuccessHandler( ...생략... ) // 로그아웃 성공 후 핸들러
					            
//					    .rememberMe(rememberMe -> rememberMe // rememberMe 기능 작동함
//					            .rememberMeParameter("saveId") // default: remember-me, checkbox 등의 이름과 맞춰야함
//					            .tokenValiditySeconds(3600) // 쿠키의 만료시간 설정(초), default: 14일
//					            .alwaysRemember(false) // 사용자가 체크박스를 활성화하지 않아도 항상 실행, default: false
//					            .userDetailsService(customUserDetailsService) // 기능을 사용할 때 사용자 정보가 필요함. 반드시 이 설정 필요함.
//                
//					    		);

        return http.build();
    }
	// 이외에도 등록해서 사용하면 된다..

}