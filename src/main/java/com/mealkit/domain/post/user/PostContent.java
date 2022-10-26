package com.mealkit.domain.post.user;

import com.mealkit.domain.constant.AuditingFields;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.*;

@ToString
@Getter
@Entity
@Setter
@Table(name = "post_content")
public class PostContent extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "content_id")
    private Long id;

    @Column(length = 1000, name="content_details")
    private String content;

}
