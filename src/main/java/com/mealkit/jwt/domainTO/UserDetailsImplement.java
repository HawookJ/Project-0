package com.mealkit.jwt.domainTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.UserAccount;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class UserDetailsImplement implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;

    @Getter
    private UserAccount userAccount;

    public UserDetailsImplement(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public UserDetailsImplement(Long id, String username,
                                String email, String password) {

        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;


    }

    public static UserDetailsImplement build(UserAccount userAccount) {

        return new UserDetailsImplement(userAccount.getUserId(),
                userAccount.getUserName(), userAccount.getUserEmail(),
                userAccount.getUserPassword());
    }

    public UserAccountDto toDto() {
        return UserAccountDto.of(
                userAccount.getUserId(),
                userAccount.getUserName(),
                userAccount.getNickName(),
                userAccount.getUserEmail(),
                userAccount.getUserChild(),
                userAccount.getUserLevel(),
                userAccount.getUserMemo(),
                password
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        userAccount.getRoleList().forEach(r -> {
            authorities.add(() -> r);
        });
        return authorities;
    }

    @Override
    public String getPassword() {

        return userAccount.getUserPassword();
    }

    @Override
    public String getUsername() {
        return userAccount.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImplement user = (UserDetailsImplement) o;
        return Objects.equals(id, user.id);
    }


}

