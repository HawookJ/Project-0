package com.mealkit.domain.post.admin;

import com.mealkit.domain.constant.AuditingFields;
import com.mealkit.domain.Board;
import com.mealkit.domain.post.user.UserComment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;


@Entity
@Getter
@ToString
public class AdminPost extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long homeId;

    private String homeName;

    @Setter
    private String homeAddress;

    private String homeNumber;

    private boolean CCTV;

    private double homeSize;

    private Integer homeChildren;

    private String homeRegister;

    private String homeVideo;

    private String homeMeal;

    @Setter
    @ManyToOne
    @JoinColumn( name = "board_id")
    private Board board;

    private Long homeView;

    private String homeDetails;

    private String homeTitle;


    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "adminPost", cascade = CascadeType.ALL)
    private final Set<AdminComment> adminComments = new LinkedHashSet<>();


    protected AdminPost() {}

    private AdminPost(String homeName, String homeAddress, String homeNumber, boolean CCTV, double homeSize, Integer homeChildren, String homeRegister, String homeVideo, String homeMeal, Long homeView, String homeDetails, String homeTitle) {
        this.homeName = homeName;
        this.homeAddress = homeAddress;
        this.homeNumber = homeNumber;
        this.CCTV = CCTV;
        this.homeSize = homeSize;
        this.homeChildren = homeChildren;
        this.homeRegister = homeRegister;
        this.homeVideo = homeVideo;
        this.homeMeal = homeMeal;
        this.homeView = homeView;
        this.homeDetails = homeDetails;
        this.homeTitle = homeTitle;

    }

    public static AdminPost of(String homeName, String homeAddress, String homeNumber, boolean CCTV,
                               double homeSize, Integer homeChildren, String homeRegister, String homeVideo,
                               String homeMeal, Long homeView, String homeDetails, String homeTitle) {
                return new AdminPost(homeName,homeAddress,homeNumber,CCTV,homeSize,homeChildren,homeRegister,homeVideo,homeMeal,homeView,homeDetails,
        homeTitle);
    }

    //    @ToString.Exclude
//    @OneToMany(mappedBy = "AdminPost", cascade = CascadeType.ALL)
//    private Set<UserLike> userLike = new LinkedHashSet<>();

}
