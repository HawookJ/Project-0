package com.mealkit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mealkit.OAuth2.domainTO.NaverToken;
import com.mealkit.OAuth2.service.NaverService;
import com.mealkit.domain.DTO.EmailDTO;
import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.RoleType;
import com.mealkit.jwt.domainTO.JwtTokens;
import com.mealkit.jwt.domainTO.MessageResponse;
import com.mealkit.jwt.domainTO.SignUpRequest;
import com.mealkit.repository.UserRepository;
import com.mealkit.service.EmailService;
import com.mealkit.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {


    private AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService ems;



    //private final KakaoService kakaoService;
    private final NaverService naverService;

// @PostMapping("/login")
// //활성화 안함.
//    public ResponseEntity<?> authenticateUser
//               (@RequestBody LoginRequest loginRequest) {
//        log.info("유저 이름 가져오기 : " +loginRequest.getUsername() + "유저 비밀번호 가져오기 : " + loginRequest.getPassword());
//
//
//
//           UserAccount userAccount = userRepository.findByUserName(loginRequest.getUsername());
////                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
//            log.info("유저에대한 정보 : " + userAccount.getUserPassword() + " and " +userAccount.getUserName());
//
//
//        if (!passwordEncoder.matches(loginRequest.getPassword() ,userAccount.getUserPassword())) {
//            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//        }
//
//        if(loginRequest.getUsername() == userAccount.getUserName() && loginRequest.getPassword() == userAccount.getUserPassword()){
//
//            Authentication authentication = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(userAccount.getUserName(), userAccount.getUserPassword()));
//            log.info(authentication.getPrincipal().toString());
//        }
//
//
//
//        JwtTokens jwtToken = jwtService.joinJwtToken(userAccount.getUserName());
//
//
//        return ResponseEntity.ok(loginRequest);
//    }

    @GetMapping("/refresh/{userName}")
    public Map<String,String> refreshToken(@PathVariable("userName") String userName, @RequestHeader("Authorization") String refreshToken,
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
        log.info("확인 : "+signUpRequest.getUsername() + " and 비밀번호 : " + signUpRequest.getPassword());
       System.out.println(userRepository.existsByUserName(signUpRequest
               .getUsername()));
        if (userRepository.existsByUserName(signUpRequest
              .getUsername())) {

            return ResponseEntity.badRequest()
                .body(new MessageResponse
                  ("Error: username is already taken!"));
        }

        if (userRepository.existsByUserEmail
                           (signUpRequest.getEmail())) {

            return ResponseEntity.badRequest()
                 .body(new MessageResponse
                        ("Error: Email is already in use!"));
        }

        UserAccount userAccount = UserAccount.builder()
                .createdAt(LocalDateTime.now())
                .userName(signUpRequest.getUsername())
                .userEmail(signUpRequest.getEmail())
                .userPassword(passwordEncoder.encode(signUpRequest.getPassword()))
                .nickName(signUpRequest.getNickName())
                .role(RoleType.USER)
                .userLevel(1)
                .userChild(signUpRequest.getChild())
                .userMemo(signUpRequest.getMemo())
                .provider("NONE")
                .build();
            userRepository.save(userAccount);

         return ResponseEntity
         .ok(new MessageResponse("user registered successfully!"));
    }

    @GetMapping("/api/oauth/token/naver")
    public Map<String, String> NaverLogin(@RequestParam("code") String code) {
        System.out.println("네이버 로그인1");
        NaverToken oauthToken = naverService.getAccessToken(code);

        UserAccount saveUser = naverService.saveUser(oauthToken.getAccess_token());

        JwtTokens jwtToken = jwtService.joinJwtToken(saveUser.getUserName());
        System.out.println("네이버 로그인2");
        return jwtService.successLoginResponse(jwtToken);
    }
    @GetMapping("/login/oauth2/code/naver")
    public String NaverCode(@RequestParam("code") String code) {
        return "네이버 로그인 인증완료, code: "  + code;
    }




    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam String userEmail) throws Exception {
        System.out.println("확인1 : " +userEmail);
        log.info("유저이메일 : " + userEmail);
    EmailDTO email = ems.sendEmail(userEmail);
    ems.mailSend(email);
    return "AdminPost/index";

    }
    @PostMapping(value = "/findUser") //이름으로 로그인하는중인데 닉네임혹은 이메일로 바꿀예정
    public String userEmail(
            @RequestParam (value = "userEmail", required = false) String userEmail) throws Exception{
        System.out.println("유저 이메일 : " + userEmail);
        UserAccount userAccount = userRepository.findByUserEmail(userEmail); //아이디찾기 위한 DTO 만들예정

        String nickName= userAccount.getNickName();
        System.out.println(nickName);

        return nickName;
    }

}