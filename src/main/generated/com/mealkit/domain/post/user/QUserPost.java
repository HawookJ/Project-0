package com.mealkit.domain.post.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserPost is a Querydsl query type for UserPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserPost extends EntityPathBase<UserPost> {

    private static final long serialVersionUID = -209066142L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserPost userPost = new QUserPost("userPost");

    public final com.mealkit.domain.constant.QAuditingFields _super = new com.mealkit.domain.constant.QAuditingFields(this);

    public final com.mealkit.domain.QBoard board;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final StringPath hashtag = createString("hashtag");

    public final NumberPath<Integer> hidePost = createNumber("hidePost", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final QPostContent postContent;

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    public final NumberPath<Integer> postLevel = createNumber("postLevel", Integer.class);

    public final NumberPath<Long> postView = createNumber("postView", Long.class);

    public final StringPath title = createString("title");

    public final SetPath<UserComment, QUserComment> userComments = this.<UserComment, QUserComment>createSet("userComments", UserComment.class, QUserComment.class, PathInits.DIRECT2);

    public final SetPath<com.mealkit.domain.post.UserLike, com.mealkit.domain.post.QUserLike> userLike = this.<com.mealkit.domain.post.UserLike, com.mealkit.domain.post.QUserLike>createSet("userLike", com.mealkit.domain.post.UserLike.class, com.mealkit.domain.post.QUserLike.class, PathInits.DIRECT2);

    public QUserPost(String variable) {
        this(UserPost.class, forVariable(variable), INITS);
    }

    public QUserPost(Path<? extends UserPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserPost(PathMetadata metadata, PathInits inits) {
        this(UserPost.class, metadata, inits);
    }

    public QUserPost(Class<? extends UserPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.mealkit.domain.QBoard(forProperty("board")) : null;
        this.postContent = inits.isInitialized("postContent") ? new QPostContent(forProperty("postContent")) : null;
    }

}

