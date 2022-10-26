package com.mealkit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mealkit.OAuth2.domainTO.NaverToken;
//import com.mealkit.OAuth2.service.KakaoService;
import com.mealkit.OAuth2.service.NaverService;
import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.RoleType;
import com.mealkit.jwt.domainTO.*;
import com.mealkit.repository.UserRepository;
import com.mealkit.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;
    @Autowired
    UserRepository userRepository;

    //private final KakaoService kakaoService;
    private final NaverService naverService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser
               (@RequestBody LoginRequest loginRequest) {
        log.info("유저 이름 가져오기 : " +loginRequest.getUsername() + "유저 비밀번호 가져오기 : " + loginRequest.getPassword());

           UserAccount userAccount = userRepository.findByUserName(loginRequest.getUsername());
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
            log.info("유저에대한 정보 : " + userAccount.getUserPassword() + " and " +userAccount.getUserName());


        if (!passwordEncoder.matches(loginRequest.getPassword() ,userAccount.getUserPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }


        JwtTokens jwtToken = jwtService.joinJwtToken(userAccount.getUserName());


        return ResponseEntity.ok(new MessageResponse("Thank You" + jwtToken));
    }

/*    @PostMapping("/refreshToken")
    public ResponseEntity refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        log.info("토큰확인하기 : " + refreshTokenDto.getRefreshToken());
        LoginResponse response = userDetailsServiceImpl.refreshToken(refreshTokenDto);
        return ResponseEntity.ok(response);
    }*/


    @GetMapping("/refresh/{userName}")
    public Map<String,String> refreshToken(@PathVariable("userName") String userName, @RequestHeader("refreshToken") String refreshToken,
                                           HttpServletResponse response) throws JsonProcessingException {
            log.info("check userName in area : "+ userName);
            log.info("check refreshToken in area : "+ refreshToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        JwtTokens jwtTokens = jwtService.validRefreshToken(userName, refreshToken);
        Map<String, String> jsonResponse = jwtService.recreateTokenResponse(jwtTokens);

        return jsonResponse;
    }



   @PostMapping("/signup")
    public ResponseEntity<?> registerUser
                  (@RequestBody SignUpRequest signUpRequest) {
        log.info("확인"+signUpRequest.getUsername());
        if (userRepository.existsByUserName(signUpRequest
              .getUsername())) {

            return ResponseEntity.badRequest()
                .body(new MessageResponse
                  ("Error: username is already taken!"));
        }

        if (userRepository.existsByEmail
                           (signUpRequest.getEmail())) {

            return ResponseEntity.badRequest()
                 .body(new MessageResponse
                        ("Error: Email is already in use!"));
        }

        UserAccount userAccount = UserAccount.builder()
                .userName(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .userPassword(passwordEncoder.encode(signUpRequest.getPassword()))
                .nickName(signUpRequest.getNickName())
                .role(RoleType.USER)
                .provider("NOT YET")
                .build();
            userRepository.save(userAccount);

         return ResponseEntity
         .ok(new MessageResponse("user registered successfully!"));
    }

    @GetMapping("/api/oauth/token/naver")
    public Map<String, String> NaverLogin(@RequestParam("code") String code) {

        NaverToken oauthToken = naverService.getAccessToken(code);

        UserAccount saveUser = naverService.saveUser(oauthToken.getAccess_token());

        JwtTokens jwtToken = jwtService.joinJwtToken(saveUser.getUserName());

        return jwtService.successLoginResponse(jwtToken);
    }
    @GetMapping("/login/oauth2/code/naver")
    public String NaverCode(@RequestParam("code") String code) {
        return "네이버 로그인 인증완료, code: "  + code;
    }


}