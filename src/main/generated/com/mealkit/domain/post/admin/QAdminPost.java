package com.mealkit.domain.post.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdminPost is a Querydsl query type for AdminPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdminPost extends EntityPathBase<AdminPost> {

    private static final long serialVersionUID = -315789052L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdminPost adminPost = new QAdminPost("adminPost");

    public final com.mealkit.domain.constant.QAuditingFields _super = new com.mealkit.domain.constant.QAuditingFields(this);

    public final SetPath<AdminPostComment, QAdminPostComment> adminPostComments = this.<AdminPostComment, QAdminPostComment>createSet("adminPostComments", AdminPostComment.class, QAdminPostComment.class, PathInits.DIRECT2);

    public final com.mealkit.domain.QBoard board;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final SetPath<com.mealkit.domain.Hashtag, com.mealkit.domain.QHashtag> hashtags = this.<com.mealkit.domain.Hashtag, com.mealkit.domain.QHashtag>createSet("hashtags", com.mealkit.domain.Hashtag.class, com.mealkit.domain.QHashtag.class, PathInits.DIRECT2);

    public final StringPath homeAddress = createString("homeAddress");

    public final BooleanPath homeCCTV = createBoolean("homeCCTV");

    public final NumberPath<Integer> homeChildren = createNumber("homeChildren", Integer.class);

    public final StringPath homeDetails = createString("homeDetails");

    public final NumberPath<Long> homeId = createNumber("homeId", Long.class);

    public final StringPath homeMeal = createString("homeMeal");

    public final StringPath homeName = createString("homeName");

    public final StringPath homeNumber = createString("homeNumber");

    public final StringPath homeRegister = createString("homeRegister");

    public final NumberPath<Double> homeSize = createNumber("homeSize", Double.class);

    public final StringPath homeTitle = createString("homeTitle");

    public final StringPath homeVideo = createString("homeVideo");

    public final NumberPath<Long> homeView = createNumber("homeView", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final com.mealkit.domain.QUserAccount userAccount;

    public QAdminPost(String variable) {
        this(AdminPost.class, forVariable(variable), INITS);
    }

    public QAdminPost(Path<? extends AdminPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdminPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdminPost(PathMetadata metadata, PathInits inits) {
        this(AdminPost.class, metadata, inits);
    }

    public QAdminPost(Class<? extends AdminPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.mealkit.domain.QBoard(forProperty("board")) : null;
        this.userAccount = inits.isInitialized("userAccount") ? new com.mealkit.domain.QUserAccount(forProperty("userAccount"), inits.get("userAccount")) : null;
    }

}

