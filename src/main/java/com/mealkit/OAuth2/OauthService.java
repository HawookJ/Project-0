/*
package com.mealkit.OAuth2;

import com.mealkit.OAuth2.service.GoogleOauth;
import com.mealkit.OAuth2.service.KakaoOauth;
import com.mealkit.OAuth2.service.NaverOauth;
import com.mealkit.OAuth2.service.SocialOauth;
import com.mealkit.domain.constant.SocialLoginType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

    private final List<SocialOauth> socialOauthList;
    private final GoogleOauth googleOauth;
    private final NaverOauth naverOauth;
    private final KakaoOauth kakaoOauth;
    private final HttpServletResponse response;

    public void request(SocialLoginType socialLoginType) {
        String redirectURL;
        switch (socialLoginType) {
            case GOOGLE: {
                log.info(" 구글 리다이랙트가 이루어지는 곳");
                redirectURL = googleOauth.getOauthRedirectURL();
            }
            break;

            case NAVER:{
                log.info(" 네이버 리다이랙트가 이루어지는 곳");
               redirectURL = naverOauth.getOauthRedirectURL();
            }break;

            case KAKAO:{
                log.info(" 카카오 리다이랙트가 이루어지는 곳");
                redirectURL = kakaoOauth.getOauthRedirectURL();
            }break;

            default: {
                throw new IllegalArgumentException("알 수없는 소셜 로그인");
            }
        }
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String requestAccessToken(SocialLoginType socialLoginType, String code) {
        switch (socialLoginType) {
            case GOOGLE: {
                return googleOauth.requestAccessToken(code);
            }

            default: {
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }
        }
    }


    private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }


}*/
