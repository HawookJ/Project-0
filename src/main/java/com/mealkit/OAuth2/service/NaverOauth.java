/*
package com.mealkit.OAuth2.service;

import com.mealkit.domain.constant.SocialLoginType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NaverOauth implements SocialOauth {

    @Value("${sns.naver.url}")
    private String NAVER_SNS_BASE_URL;
    @Value("${sns.naver.client.id}")
    private String NAVER_SNS_CLIENT_ID;
    @Value("${sns.naver.callback.url}")
    private String NAVER_SNS_CALLBACK_URL;
    @Value("${sns.naver.client.secret}")
    private String NAVER_SNS_CLIENT_SECRET;
    @Value("${sns.naver.token-uri}")
    private String NAVER_SNS_TOKEN_BASE_URL;




    @Override
    public String getOauthRedirectURL() {


        return null;
    }

    @Override
    public String requestAccessToken(String code) {
        return null;
    }

    @Override
    public SocialLoginType type() {
        return SocialOauth.super.type();
    }
}
*/
