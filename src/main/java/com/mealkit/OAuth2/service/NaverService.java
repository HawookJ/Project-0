package com.mealkit.OAuth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealkit.OAuth2.domainTO.NaverProfile;
import com.mealkit.OAuth2.domainTO.NaverToken;
import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.RoleType;
import com.mealkit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NaverService {

    private final UserRepository userRepository;

    private final String client_id = "4U0dG6smZXClFTARNuMJ";
    private final String client_secret = "p3PqTcCfrg";
    private final String redirect_uri = "http://localhost:9011/login/oauth2/code/naver";
    private final String accessTokenUri = "https://nid.naver.com/oauth2.0/token";
    private final String UserInfoUri = "https://openapi.naver.com/v1/nid/me";


    public NaverToken getAccessToken(String code) {

        //요청 param (body)
        MultiValueMap<String , String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id",client_id );
        params.add("redirect_uri",redirect_uri);
        params.add("code", code);
        params.add("client_secret", client_secret);


        //request
        WebClient wc = WebClient.create(accessTokenUri);
        String response = wc.post()
                .uri(accessTokenUri)
                .body(BodyInserters.fromFormData(params))
                .header("Content-type","application/x-www-form-urlencoded;charset=utf-8" ) //요청 헤더
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //json형태로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        NaverToken naverToken =null;

        try {
            naverToken = objectMapper.readValue(response, NaverToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return naverToken;
    }

    /**
     * 사용자 정보 가져오기
     */
    public NaverProfile findProfile(String token) {

        //Http 요청
        WebClient wc = WebClient.create(UserInfoUri);
        String response = wc.get()
                .uri(UserInfoUri)
                .header("Authorization", "Bearer " + token)
                .header("Content-type", "application/xml;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        NaverProfile naverProfile = null;

        try {
            naverProfile = objectMapper.readValue(response, NaverProfile.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return naverProfile;
    }

    @Transactional
    public UserAccount saveUser(String access_token) {
        NaverProfile profile = findProfile(access_token); //사용자 정보 받아오기
        UserAccount userAccount = userRepository.findByUserName(profile.response.getName());

        //처음이용자 강제 회원가입
        if(userAccount ==null) {
            userAccount = UserAccount.builder()
                    .userName(profile.response.getId())
                    .userPassword(null) //필요없으니 일단 아무거도 안넣음. 원하는데로 넣으면 됌
                    .nickName(profile.response.getNickname())
                   // .profileImg(profile.response.profile_image)
                    .userEmail(profile.response.email)
                    .role(RoleType.USER)
                   // .createTime(LocalDateTime.now())
                    .provider("Naver")
                    .build();

            userRepository.save(userAccount);
        }

        return userAccount;
    }

}