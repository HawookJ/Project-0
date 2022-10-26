package com.mealkit.domain.post.user;

import com.mealkit.domain.constant.AuditingFields;
import com.mealkit.domain.post.admin.AdminPost;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Table(indexes = {
        @Index(columnList = "commentContent"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
public class UserComment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String commentContent;

    @JoinColumn(name = "userPost_id")
    @ManyToOne //댓글을 통해 글을 볼순 잇으니 Exclude 는 반대쪽에...댓글이 포스트로 가서 찍을떄 다시 순환해서 안옴.
    private UserPost userPost;

    protected UserComment(){}


    private UserComment(String commentContent, UserPost userpost){
        this.commentContent=commentContent;
        this.userPost= userpost;
    }

public static UserComment of(String commentContent, UserPost userPost) {
    return new UserComment(commentContent, userPost);
}

    @Override 
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserComment that)) return false;
        return commentId != null && commentId.equals(that.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }
}
