package com.mealkit.domain.post.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPostContent is a Querydsl query type for PostContent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostContent extends EntityPathBase<PostContent> {

    private static final long serialVersionUID = 1592950786L;

    public static final QPostContent postContent = new QPostContent("postContent");

    public final com.mealkit.domain.constant.QAuditingFields _super = new com.mealkit.domain.constant.QAuditingFields(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public QPostContent(String variable) {
        super(PostContent.class, forVariable(variable));
    }

    public QPostContent(Path<? extends PostContent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPostContent(PathMetadata metadata) {
        super(PostContent.class, metadata);
    }

}

