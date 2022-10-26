package com.mealkit.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealkit.domain.UserAccount;
import com.mealkit.jwt.domainTO.JwtTokens;
import com.mealkit.jwt.domainTO.UserDetailsImplement;
import com.mealkit.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;




    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println(" 로그인시 AuthenticationFilter 작동!!");

        //userName, userPassword 받기 using json
        ObjectMapper om = new ObjectMapper();
        UserAccount userAccount = null;
        try {
            userAccount = om.readValue(request.getInputStream(), UserAccount.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("AuthenticationFilter :" + userAccount);
        log.info("유저 어카운트 확인 : " + userAccount.getUserName());

        // authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의 -> authenticationManager 를 사용할때 UserDetailSevice 를 사용가능
        // loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
        // UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과
        // UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
        // Authentication 객체를 만들어서 필터체인으로 리턴해준다.

        // Tip: 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
        // Tip: 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
        // 결론은 인증 프로바이더에게 알려줄 필요가 없음.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userAccount.getUserName(), userAccount.getUserPassword());
        log.info("check authenticationManager in place : " + authenticationToken.getName());



        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        log.info(authentication.getName());

        UserDetailsImplement userDetailsImplement = (UserDetailsImplement) authentication.getPrincipal();

        log.info("여기 안오는듯 한데 : " + userDetailsImplement.getUsername());

        System.out.println("필터 확인 JwtAuthenticationFilter: " + userDetailsImplement.getUsername());

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        System.out.println("successfulAuthentication HERE");
        UserDetailsImplement userDetailsImplement = (UserDetailsImplement) authResult.getPrincipal();

        //token 생성
        JwtTokens jwtTokens = jwtService.joinJwtToken(userDetailsImplement.getUserAccount().getUserName());

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, String> jsonResponse = jwtService.successLoginResponse(jwtTokens);
        String result = objectMapper.writeValueAsString(jsonResponse);

        //response 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);

        //response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+ jwtToken);
    }

}


