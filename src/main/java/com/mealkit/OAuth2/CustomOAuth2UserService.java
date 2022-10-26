/*

package com.mealkit.OAuth2;

import com.mealkit.domain.UserAccount;
import com.mealkit.repository.UserRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession session;



    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.info("지금 로드 유저까지 안오는듯한 느낌적인 느낌");

        Assert.notNull(userRequest,"userRequest cannot be null");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = service.loadUser(userRequest);

        //아이디 구분 (구글 카카오 네이버)
        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();
        log.info(registrationId);

        //구글 :sub
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        log.info(userNameAttributeName);


        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        log.info("소셜 로그인 확인 : "+ oAuthAttributes);


        UserAccount userAccount = saveOrUpdate(oAuthAttributes);
        log.info("유저 정보 저장 유무 확인 : " + userAccount);

        session.setAttribute("userAccount", new SessionUserAccount(userAccount));


        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(userAccount.getRole().getName())),
                oAuthAttributes.getAttributes(),
                oAuthAttributes.getNameAttributeKey());
    }
    private UserAccount saveOrUpdate (OAuthAttributes attributes) {
        UserAccount userAccount = userRepository.findByEmail(attributes.getEmail())
                .map(entity->entity.update(attributes.getUsername(), attributes.getEmail()))
                        .orElse(attributes.toEntity());

                return userRepository.save(userAccount);
    }




}

*/
