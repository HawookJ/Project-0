package com.mealkit.domain.post.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdminPostComment is a Querydsl query type for AdminPostComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdminPostComment extends EntityPathBase<AdminPostComment> {

    private static final long serialVersionUID = -161175365L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdminPostComment adminPostComment = new QAdminPostComment("adminPostComment");

    public final com.mealkit.domain.constant.QAuditingFields _super = new com.mealkit.domain.constant.QAuditingFields(this);

    public final StringPath adminCommentContent = createString("adminCommentContent");

    public final QAdminPost adminPost;

    public final NumberPath<Long> adminPostCommentId = createNumber("adminPostCommentId", Long.class);

    public final SetPath<AdminPostComment, QAdminPostComment> childComments = this.<AdminPostComment, QAdminPostComment>createSet("childComments", AdminPostComment.class, QAdminPostComment.class, PathInits.DIRECT2);

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

    public QAdminPostComment(String variable) {
        this(AdminPostComment.class, forVariable(variable), INITS);
    }

    public QAdminPostComment(Path<? extends AdminPostComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdminPostComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdminPostComment(PathMetadata metadata, PathInits inits) {
        this(AdminPostComment.class, metadata, inits);
    }

    public QAdminPostComment(Class<? extends AdminPostComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.adminPost = inits.isInitialized("adminPost") ? new QAdminPost(forProperty("adminPost"), inits.get("adminPost")) : null;
        this.userAccount = inits.isInitialized("userAccount") ? new com.mealkit.domain.QUserAccount(forProperty("userAccount"), inits.get("userAccount")) : null;
    }

}

