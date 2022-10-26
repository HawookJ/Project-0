/*

package com.mealkit.OAuth2;

import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.RoleType;
import com.mealkit.domain.constant.SocialLoginType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OAuthAttributes {
    private Map<String,Object> attributes;
    private String nameAttributeKey;
    private String username;
    private String nickname;
    @NotNull
    @Enumerated(EnumType.STRING)
    private SocialLoginType socialLoginType;
    private String email;
    private RoleType role;


    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes){
        if((registrationId).equals("naver")){

            return ofNaver("id", attributes);
            }
            return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes){
        return OAuthAttributes.builder()
                .username((String) attributes.get("username"))
                .email((String) attributes.get("email"))
                .nickname((String) attributes.get("nickname"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName,  Map<String, Object> attributes){
        Map<String , Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .username((String) attributes.get("username"))
                .email((String) attributes.get("email"))
                .nickname((String) attributes.get("nickname"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }





 public UserAccount toEntity(){
        return UserAccount.builder()
                .userName(username)
                .email(email)
           //     .nickName(nickname)
                .role(RoleType.USER)
                .build();
 }



}

*/
