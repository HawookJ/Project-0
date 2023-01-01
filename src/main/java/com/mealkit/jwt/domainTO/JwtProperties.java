package com.mealkit.jwt.domainTO;

public interface JwtProperties {

    String SECRET = "the_secret_key"; //우리 서버만 알고 있는 비밀값
    int AccessToken_TIME =  6000000; // (1/1000초)
    int RefreshToken_TIME = 10000000 ;
    String HEADER_STRING = "Authorization";
    String TOKEN_PREFIX = "Bearer ";
}