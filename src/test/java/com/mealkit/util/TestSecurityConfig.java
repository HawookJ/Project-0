package com.mealkit.util;

import com.mealkit.config.SecurityConfig;
import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.RoleType;
import com.mealkit.repository.UserRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private UserRepository userRepository;

    @BeforeTestMethod
    public void securitySetUp() {
        given(userRepository.findById(5L)).willReturn(Optional.of(UserAccount.of(
                5L,
                "Hawok",
                12,
                "pw@emainl",
                "uno-childe",
                "papwwsord",
                "testnickname",
                "userNemenmo",
                "naver",
                RoleType.USER
        )));
    }

}