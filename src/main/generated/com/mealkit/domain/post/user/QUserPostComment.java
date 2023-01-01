package com.mealkit.domain.post.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserPostComment is a Querydsl query type for UserPostComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserPostComment extends EntityPathBase<UserPostComment> {

    private static final long serialVersionUID = 1072032925L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserPostComment userPostComment = new QUserPostComment("userPostComment");

    public final com.mealkit.domain.constant.QAuditingFields _super = new com.mealkit.domain.constant.QAuditingFields(this);

    public final SetPath<UserPostComment, QUserPostComment> childComments = this.<UserPostComment, QUserPostComment>createSet("childComments", UserPostComment.class, QUserPostComment.class, PathInits.DIRECT2);

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

    public final NumberPath<Long> parentCommentId = createNumber("parentCommentId", Long.class);

    public final com.mealkit.domain.QUserAccount userAccount;

    public final QUserPost userPost;

    public QUserPostComment(String variable) {
        this(UserPostComment.class, forVariable(variable), INITS);
    }

    public QUserPostComment(Path<? extends UserPostComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserPostComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserPostComment(PathMetadata metadata, PathInits inits) {
        this(UserPostComment.class, metadata, inits);
    }

    public QUserPostComment(Class<? extends UserPostComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userAccount = inits.isInitialized("userAccount") ? new com.mealkit.domain.QUserAccount(forProperty("userAccount"), inits.get("userAccount")) : null;
        this.userPost = inits.isInitialized("userPost") ? new QUserPost(forProperty("userPost"), inits.get("userPost")) : null;
    }

}

