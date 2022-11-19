package com.mealkit.jwt.domainTO;

public interface JwtProperties {

    String SECRET = "the_secret_key"; //우리 서버만 알고 있는 비밀값
    int AccessToken_TIME =  600000; // (1/1000초)
    int RefreshToken_TIME = 2000000 ;
    String HEADER_STRING = "accessToken";
}