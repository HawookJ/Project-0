package com.mealkit.domain.post.admin;

import com.mealkit.domain.Board;
import com.mealkit.domain.Hashtag;
import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.AuditingFields;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@Entity
@Getter
@Table(indexes = {
        @Index(columnList = "homeTitle"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@ToString
public class AdminPost extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_id")
    private Long homeId;
    @Setter
    private String homeName;

    @Setter
    private String homeAddress;
    @Setter
    private String homeNumber;
    @Setter
    private boolean homeCCTV;
    @Setter
    private double homeSize;
    @Setter
    private Integer homeChildren;
    @Setter
    private String homeRegister;
    @Setter
    private String homeVideo;
    @Setter
    private String homeMeal;

    @Setter
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
    @Setter
    private Long homeView;
    @Setter
    private String homeDetails;
    @Setter
    private String homeTitle;


    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "adminPost", cascade = CascadeType.ALL)
    private final Set<AdminPostComment> adminPostComments = new LinkedHashSet<>();


    @Setter
    @JoinColumn(name = "user_id")
    @ManyToOne(optional = false)
    private UserAccount userAccount; // 유저 정보 (ID)

    @ToString.Exclude
    @JoinTable(
            name = "home_hashtag",
            joinColumns = @JoinColumn(name = "home_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_Id")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Hashtag> hashtags = new LinkedHashSet<>();


    protected AdminPost() {
    }

    private AdminPost(UserAccount userAccount, String homeName, String homeAddress, String homeNumber, boolean homeCCTV, double homeSize, Integer homeChildren, String homeRegister, String homeVideo, String homeMeal, Long homeView, String homeDetails, String homeTitle) {
        this.userAccount = userAccount;
        this.homeName = homeName;
        this.homeAddress = homeAddress;
        this.homeNumber = homeNumber;
        this.homeCCTV = homeCCTV;
        this.homeSize = homeSize;
        this.homeChildren = homeChildren;
        this.homeRegister = homeRegister;
        this.homeVideo = homeVideo;
        this.homeMeal = homeMeal;
        this.homeView = homeView;
        this.homeDetails = homeDetails;
        this.homeTitle = homeTitle;

    }

    public static AdminPost of(UserAccount userAccount, String homeName, String homeAddress, String homeNumber, boolean homeCCTV,
                               double homeSize, Integer homeChildren, String homeRegister, String homeVideo,
                               String homeMeal, Long homeView, String homeDetails, String homeTitle) {
        return new AdminPost(userAccount, homeName, homeAddress, homeNumber, homeCCTV, homeSize, homeChildren, homeRegister, homeVideo, homeMeal, homeView, homeDetails,
                homeTitle);
    }

    //    @ToString.Exclude
//    @OneToMany(mappedBy = "AdminPost", cascade = CascadeType.ALL)
//    private Set<UserLike> userLike = new LinkedHashSet<>();

    public void addHashtag(Hashtag hashtag) {
        this.getHashtags().add(hashtag);
    }

    public void addHashtags(Collection<Hashtag> hashtags) {
        this.getHashtags().addAll(hashtags);
    }

    public void clearHashtags() {
        this.getHashtags().clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminPost that)) return false;
        return this.getHomeId() != null && this.getHomeId().equals(that.getHomeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getHomeId());
    }

}
