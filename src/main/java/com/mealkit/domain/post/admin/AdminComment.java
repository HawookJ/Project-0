package com.mealkit.domain.post.admin;

import com.mealkit.domain.constant.AuditingFields;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Table(indexes = {
        @Index(columnList = "adminCommentContent"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
public class AdminComment extends AuditingFields {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AdminCommentId;

    private String adminCommentContent;

    @JoinColumn(name = "home_id")
    @ManyToOne //댓글을 통해 글을 볼순 잇으니 Exclude 는 반대쪽에...댓글이 포스트로 가서 찍을떄 다시 순환해서 안옴.
    private AdminPost adminPost;

    protected AdminComment(){}


    private AdminComment(String adminCommentContent, AdminPost adminPost){
        this.adminCommentContent=adminCommentContent;
        this.adminPost= adminPost;
    }

    public static AdminComment of(String commentContent, AdminPost adminPost) {
        return new AdminComment(commentContent, adminPost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminComment that)) return false;
        return adminCommentContent != null && adminCommentContent.equals(that.adminCommentContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminCommentContent);
    }
}
