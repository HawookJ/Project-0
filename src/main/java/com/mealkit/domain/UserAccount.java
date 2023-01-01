package com.mealkit.domain;


import com.mealkit.domain.constant.RoleType;
import com.mealkit.jwt.domainTO.RefreshToken;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(indexes = {
        @Index(columnList = "user_email", unique = true),
        @Index(columnList = "createdAt")

})
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    @Setter
    @Column(unique = true, length = 50, name="user_name")
    private String userName;

    @Setter
    @Column(length = 100, name="user_nickname", nullable = false)
    private String nickName;

    @Setter
    @Column(length = 100, name="user_email", nullable = false)
    private String userEmail;

    @Setter
    @Column(name = "user_child")
    private String userChild;

    @Setter
    @Column(name = "user_level")
    private Integer userLevel;


    @Column(name = "user_provider")
    private String provider;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Setter
    @Column(name = "user_memo")
    private String userMemo;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "refreshToken")
    private RefreshToken refreshToken;

    @Column(name = "user_password")
    @Setter
    private String userPassword;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
  //  @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일시

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
   // @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정일시


    public void createRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }
    public void SetRefreshToken(String refreshToken) {
        this.refreshToken.setRefreshToken(refreshToken);
    }


    public List<String> getRoleList() {
        if(this.role.getName().length()>0) {
            return Arrays.asList(this.role.getName().split(","));
        }
        return new ArrayList<>();
    }


//나중에 확인.


    public UserAccount update(String userName, String userEmail){
        this.userName =userName;
        this.userEmail= userEmail;
        return this;
}


    @Builder
    public UserAccount(LocalDateTime createdAt, String userName, Integer userLevel, String userEmail, String userChild, String userPassword,String nickName,String userMemo, String provider, RoleType role){
        this.createdAt=createdAt;
        this.userName=userName;
        this.userLevel=userLevel;
        this.userEmail=userEmail;
        this.userChild=userChild;
        this.userPassword= userPassword;
        this.nickName = nickName;
        this.userMemo = userMemo;
        this.provider=provider;
        this.role= role;
    }

    public UserAccount(Long userId, String userName, Integer userLevel, String userEmail, String userChild, String userPassword, String nickName, String userMemo, String provider, RoleType role) {
        this.userId=userId;
        this.userName=userName;
        this.userLevel=userLevel;
        this.userEmail=userEmail;
        this.userChild=userChild;
        this.userPassword= userPassword;
        this.nickName = nickName;
        this.userMemo = userMemo;
        this.provider=provider;
        this.role= role;
    }

    public static UserAccount of(Long userId, String userName, Integer userLevel, String userEmail, String userChild, String userPassword,String nickName,String userMemo, String provider, RoleType role){
        return new UserAccount(userId,userName,userLevel,userEmail,userChild,userPassword,nickName,userMemo,provider,role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return this.getUserId() != null && this.getUserId().equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUserId());
    }


}
