package com.mealkit.jwt.domainTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class SignUpRequest {

    private String username;
    private String email;
    private String password;
    private String nickName;
    private String child;
    private String memo;



}