package com.mealkit.domain.post;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserLike is a Querydsl query type for UserLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserLike extends EntityPathBase<UserLike> {

    private static final long serialVersionUID = -2023378898L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserLike userLike = new QUserLike("userLike");

    public final com.mealkit.domain.constant.QAuditingFields _super = new com.mealkit.domain.constant.QAuditingFields(this);

    public final com.mealkit.domain.post.admin.QAdminPost adminPost;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final com.mealkit.domain.QUserAccount userAccount;

    public final com.mealkit.domain.post.user.QUserPost userPost;

    public final com.mealkit.domain.post.user.QUserPostComment userPostComment;

    public QUserLike(String variable) {
        this(UserLike.class, forVariable(variable), INITS);
    }

    public QUserLike(Path<? extends UserLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserLike(PathMetadata metadata, PathInits inits) {
        this(UserLike.class, metadata, inits);
    }

    public QUserLike(Class<? extends UserLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.adminPost = inits.isInitialized("adminPost") ? new com.mealkit.domain.post.admin.QAdminPost(forProperty("adminPost"), inits.get("adminPost")) : null;
        this.userAccount = inits.isInitialized("userAccount") ? new com.mealkit.domain.QUserAccount(forProperty("userAccount"), inits.get("userAccount")) : null;
        this.userPost = inits.isInitialized("userPost") ? new com.mealkit.domain.post.user.QUserPost(forProperty("userPost"), inits.get("userPost")) : null;
        this.userPostComment = inits.isInitialized("userPostComment") ? new com.mealkit.domain.post.user.QUserPostComment(forProperty("userPostComment"), inits.get("userPostComment")) : null;
    }

}

