package com.mealkit.config;



//import com.mealkit.OAuth2.CustomOAuth2UserService;


import com.mealkit.jwt.filter.AuthenticationFilter;
import com.mealkit.jwt.filter.AuthorizationFilter;
import com.mealkit.repository.UserRepository;
import com.mealkit.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;



@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

 private final UserRepository userRepository;
 private final JwtService jwtService;

 //   private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers("/oauth/**", "/sendEmail", "/api/oauth/token/kakao","/login/oauth2/code/kakao","/signup")
                .antMatchers("/Find/**","/findId", "/login/oauth2/code/naver", "/api/oauth/token/naver", "/adminPosts" ,
                        "/adminPost/**","/refresh/**","/findPw","/adminPosts/form" )
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }





    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable().formLogin().disable();
        http.apply(new MyCustomDsl()); //.and().formLogin().loginPage("/login").permitAll();
        http.cors().disable().csrf().disable().exceptionHandling().and()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            //    .and()
             //   .apply(new MyCustomDsl())
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/test/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/sendEmail").permitAll()
                .antMatchers("/static/**").permitAll()

//hasRole 아직 추가 안함

                .anyRequest()
                        .permitAll();

                 //http.oauth2Login()
                //.userInfoEndpoint().userService(customOAuth2UserService)
               // .and()
              //  .successHandler(configSuccessHandler()) //추후 추가 예정
               // .failureHandler(configFailureHandler()) //추후 추가 예정
//                .permitAll();

           return http.build();
    }


    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
log.info("why not running");
            http
                   // .addFilter(config.corsFilter())
                    .addFilter(new AuthenticationFilter(authenticationManager, jwtService)) //AuthenticationManger가 있어야 된다.(파라미터로)
                    .addFilter(new AuthorizationFilter(authenticationManager, userRepository, jwtService));
        }
    }





/*   
 리액트 사용시
 @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/


}