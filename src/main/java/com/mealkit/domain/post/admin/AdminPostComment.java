package com.mealkit.domain.post.admin;

import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.AuditingFields;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@Entity
@Getter
@Table(indexes = {
        @Index(columnList = "adminCommentContent"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
public class AdminPostComment extends AuditingFields {

    protected AdminPostComment(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminPostCommentId;

    @Setter
    private String adminCommentContent;

    @Setter
    @Column(updatable = false)
    private Long parentCommentId; //부모댓글

    @Setter
    @JoinColumn(name = "home_id")
    @ManyToOne(optional = false) //댓글을 통해 글을 볼순 잇으니 Exclude 는 반대쪽에...댓글이 포스트로 가서 찍을떄 다시 순환해서 안옴.
    private AdminPost adminPost;


    @Setter
    @JoinColumn(name = "user_id")
    @ManyToOne(optional = false)
    private UserAccount userAccount; // 유저 정보 (ID)

    @ToString.Exclude
    @OrderBy("createdAt ASC")
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL)
    private Set<AdminPostComment> childComments = new LinkedHashSet<>(); //대댓



    private AdminPostComment(UserAccount userAccount, String adminCommentContent, AdminPost adminPost, Long parentCommentId ){
        this.userAccount=userAccount;
        this.adminCommentContent=adminCommentContent;
        this.adminPost= adminPost;
        this.parentCommentId=parentCommentId;
    }

    public static AdminPostComment of(UserAccount userAccount, String commentContent, AdminPost adminPost) {
        return new AdminPostComment(userAccount, commentContent, adminPost, null);
    }

    public void addChildComment(AdminPostComment child) {
        child.setParentCommentId(this.getAdminPostCommentId());
        this.getChildComments().add(child);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminPostComment that)) return false;
        return this.getAdminPostCommentId() != null && this.getAdminPostCommentId().equals(that.getAdminPostCommentId());
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.getAdminPostCommentId());
    }
}
