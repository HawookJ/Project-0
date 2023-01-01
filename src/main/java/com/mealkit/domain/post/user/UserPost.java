package com.mealkit.domain.post.user;

import com.mealkit.domain.Hashtag;
import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.AuditingFields;
import com.mealkit.domain.Board;
import com.mealkit.domain.post.UserLike;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


@AllArgsConstructor
@ToString
@Getter
@Table(indexes = {
        @Index(columnList = "title"),

        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")})
@Entity
public class UserPost extends AuditingFields {

    @Id
    @Column(name = "userPost_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPostId;


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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Hashtag> hashtags = new LinkedHashSet<>();


    @Setter
    @JoinColumn(name = "user_id")
    @ManyToOne(optional = false)
    private UserAccount userAccount; // 유저 정보 (ID)

    @ToString.Exclude //메모리를 위해서 & 굳이 포스트를 통해서 댓글을 다 뽑을필요업음
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "userPost", cascade = CascadeType.ALL) //사실 댓글을 백업하는게 좋다
    //중복 허용x 컬렉션으로 보기
    private final Set<UserPostComment> userPostComments = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "userPost", cascade = CascadeType.ALL)
    private final Set<UserLike> userLike = new LinkedHashSet<>();

    public UserPost(UserAccount userAccount, String title, PostContent postContent, Integer hidePost, Integer postLevel, Long postView) {
        this.userAccount=userAccount;
        this.title=title;
        this.postContent=postContent;
        this.hidePost=hidePost;
        this.postLevel = postLevel;
        this.postView=postView;
    }


    //새로운 포스트 만들때 가이드 역할.
    public static  UserPost of(UserAccount userAccount, String title, PostContent postContent, Integer hidePost, Integer postLevel, Long postView){
        return new UserPost(userAccount, title, postContent, hidePost, postLevel, postView);
    }

    //중복요소 없애기, 정렬, 비교 jpa EqualsHashCode하면 전체적으로 만들어서 비효율적. 그러므로 Id로만 비교할것이다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPost userPost)) return false;
        return userPostId != null && userPostId.equals(userPost.userPostId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userPostId);
    }



}
