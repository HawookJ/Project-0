package com.mealkit.domain.post.user;

import com.mealkit.domain.constant.AuditingFields;
import com.mealkit.domain.Board;
import com.mealkit.domain.post.UserLike;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


@ToString
@Getter
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")})
@Entity
public class UserPost extends AuditingFields {

    @Id
    @Column(name = "userPost_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;


    @Setter
    @Column(nullable=false)
    private String title;

    @OneToOne
    @Setter
    @JoinColumn(name= "content_id")
    private PostContent postContent;

    @ManyToOne
    @Setter
    @JoinColumn(name= "board_id")
    private Board board;

    protected UserPost(){};

    @Setter
    private Integer postLevel;

    @Setter
    private Integer hidePost;

    @Setter
    private Long postView;

    private String hashtag;


    @ToString.Exclude //메모리를 위해서 & 굳이 포스트를 통해서 댓글을 다 뽑을필요업음
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "userPost", cascade = CascadeType.ALL) //사실 댓글을 백업하는게 좋다
    //중복 허용x 컬렉션으로 보기
    private final Set<UserComment> userComments = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "userPost", cascade = CascadeType.ALL)
    private final Set<UserLike> userLike = new LinkedHashSet<>();








    public UserPost(String title, String hashtag, PostContent postContent) {
        this.title=title;
        this.hashtag=hashtag;
        this.postContent=postContent;
    }


    //새로운 포스트 만들때 가이드 역할.
    public static  UserPost of(String title, String hashtag, PostContent postContent){
        return new UserPost(title, hashtag,postContent);
    }

    //중복요소 없애기, 정렬, 비교 jpa EqualsHashCode하면 전체적으로 만들어서 비효율적. 그러므로 Id로만 비교할것이다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPost userPost)) return false;
        return postId != null && postId.equals(userPost.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId);
    }



}
