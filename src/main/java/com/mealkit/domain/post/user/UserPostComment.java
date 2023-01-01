package com.mealkit.domain.post.user;

import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.AuditingFields;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Table(indexes = {
        @Index(columnList = "commentContent"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
public class UserPostComment extends AuditingFields {
    protected UserPostComment() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Setter
    @JoinColumn(name = "user_id")
    @ManyToOne(optional = false)
    private UserAccount userAccount; // 유저 정보 (ID)
    private String commentContent;

    @Setter
    @Column(updatable = false)
    private Long parentCommentId;

    @ToString.Exclude
    @OrderBy("createdAt ASC")
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL)
    private Set<UserPostComment> childComments = new LinkedHashSet<>();

    public void addChildComment(UserPostComment child) {
        child.setParentCommentId(this.getCommentId());
        this.getChildComments().add(child);
    }

    @JoinColumn(name = "userPost_id")
    @ManyToOne //댓글을 통해 글을 볼순 잇으니 Exclude 는 반대쪽에...댓글이 포스트로 가서 찍을떄 다시 순환해서 안옴.
    private UserPost userPost;



    private UserPostComment(UserAccount userAccount, String commentContent, UserPost userpost, Long parentCommentId) {
        this.userAccount = userAccount;
        this.commentContent = commentContent;
        this.parentCommentId = parentCommentId;
        this.userPost = userpost;
    }

    public static UserPostComment of(UserAccount userAccount, String commentContent, Long parentCommentId, UserPost userPost) {
        return new UserPostComment(userAccount , commentContent, userPost,null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPostComment that)) return false;
        return this.getCommentId() != null && this.getCommentId().equals(that.getCommentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }
}
