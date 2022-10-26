package com.mealkit.domain;


import com.mealkit.domain.constant.AuditingFields;
import com.mealkit.domain.constant.RoleType;
import com.mealkit.jwt.domainTO.RefreshToken;
import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(indexes = {
        @Index(columnList = "user_email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
public class UserAccount extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    @Setter
    @Column(unique = true, length = 50, name="user_name")
    private String userName;

    @Setter
    @Column(length = 100, name="user_nickname")
    private String nickName;

    @Setter
    @Column(length = 100, name="user_email")
    private String email;

    private String userChild;

    @Column(name = "user_provider")
    private String provider;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private RoleType role;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "refreshToken")
    private RefreshToken refreshToken;

    @Column(name = "user_password")
    @Setter
    private String userPassword;


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


    public UserAccount update(String userName, String email){
        this.userName =userName;
        this.email= email;
        return this;
}


    @Builder
    public UserAccount(String userName, String email, String userChild, String userPassword,String nickName, String provider, RoleType role){
        this.userName=userName;
        this.email=email;
        this.userChild=userChild;
        this.userPassword= userPassword;
        this.nickName = nickName;
        this.provider=provider;
        this.role= role;
    }
}
