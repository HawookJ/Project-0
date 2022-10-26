package com.mealkit.service;

import com.mealkit.domain.UserAccount;
import com.mealkit.jwt.domainTO.UserDetailsImplement;
import com.mealkit.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println("UserDetailsService loadUserByUsername 실행중");

        UserAccount findUser = userRepository.findByUserName(userName);

        return new UserDetailsImplement(findUser);
    }
}