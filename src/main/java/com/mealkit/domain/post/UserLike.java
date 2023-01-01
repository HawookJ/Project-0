package com.mealkit.domain.post;

import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.AuditingFields;
import com.mealkit.domain.post.admin.AdminPost;
import com.mealkit.domain.post.user.UserPostComment;
import com.mealkit.domain.post.user.UserPost;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@ToString
public class UserLike extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserPost userPost;

    @ManyToOne
    private AdminPost adminPost;

    @ManyToOne
    private UserPostComment userPostComment;

    @ManyToOne
    private UserAccount userAccount;


}
