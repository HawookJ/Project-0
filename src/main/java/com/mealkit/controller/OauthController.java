/*
package com.mealkit.controller;

import com.mealkit.OAuth2.OauthService;
import com.mealkit.OAuth2.SocialLoginTypeConverter;
import com.mealkit.OAuth2.service.SocialOauth;
import com.mealkit.domain.constant.SocialLoginType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class OauthController {

    private SocialLoginTypeConverter socialLoginTypeConverter;
    private final OauthService oauthService;



    @GetMapping(value= "/{socialLoginType}")
    public void socialLoginType(
            @PathVariable(name="socialLoginType")SocialLoginType socialLoginType){
        log.info("socialLoginType 확인======================================== : " + socialLoginType);
        //    socialLoginType = socialLoginTypeConverter.convert(String.valueOf(socialLoginType));
            log.info("요청받음 :: Social Login : " + socialLoginType);
            oauthService.request(socialLoginType);

    }
    @GetMapping(value = "/{socialLoginType}/callback")
    public String callback(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
            @RequestParam(name = "code") String code) {
        log.info("socialLoginType 확인======================================== : " + socialLoginType);
        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);
        return oauthService.requestAccessToken(socialLoginType, code);
    }


}
*/
