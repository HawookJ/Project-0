package com.mealkit.domain.post.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdminComment is a Querydsl query type for AdminComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdminComment extends EntityPathBase<AdminComment> {

    private static final long serialVersionUID = -351564933L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdminComment adminComment = new QAdminComment("adminComment");

    public final com.mealkit.domain.constant.QAuditingFields _super = new com.mealkit.domain.constant.QAuditingFields(this);

    public final StringPath adminCommentContent = createString("adminCommentContent");

    public final NumberPath<Long> AdminCommentId = createNumber("AdminCommentId", Long.class);

    public final QAdminPost adminPost;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public QAdminComment(String variable) {
        this(AdminComment.class, forVariable(variable), INITS);
    }

    public QAdminComment(Path<? extends AdminComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdminComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdminComment(PathMetadata metadata, PathInits inits) {
        this(AdminComment.class, metadata, inits);
    }

    public QAdminComment(Class<? extends AdminComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.adminPost = inits.isInitialized("adminPost") ? new QAdminPost(forProperty("adminPost"), inits.get("adminPost")) : null;
    }

}

