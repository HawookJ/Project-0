package com.mealkit.domain.post.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserComment is a Querydsl query type for UserComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserComment extends EntityPathBase<UserComment> {

    private static final long serialVersionUID = 754847837L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserComment userComment = new QUserComment("userComment");

    public final com.mealkit.domain.constant.QAuditingFields _super = new com.mealkit.domain.constant.QAuditingFields(this);

    public final StringPath commentContent = createString("commentContent");

    public final NumberPath<Long> commentId = createNumber("commentId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final QUserPost userPost;

    public QUserComment(String variable) {
        this(UserComment.class, forVariable(variable), INITS);
    }

    public QUserComment(Path<? extends UserComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserComment(PathMetadata metadata, PathInits inits) {
        this(UserComment.class, metadata, inits);
    }

    public QUserComment(Class<? extends UserComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userPost = inits.isInitialized("userPost") ? new QUserPost(forProperty("userPost"), inits.get("userPost")) : null;
    }

}

