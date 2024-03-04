package com.team5.projrental.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDisputeChatUser is a Querydsl query type for DisputeChatUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDisputeChatUser extends EntityPathBase<DisputeChatUser> {

    private static final long serialVersionUID = 1893573828L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDisputeChatUser disputeChatUser = new QDisputeChatUser("disputeChatUser");

    public final QDispute _super;

    // inherited
    public final QAdmin admin;

    public final QChatUser chatUser;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt;

    //inherited
    public final StringPath details;

    //inherited
    public final NumberPath<Long> id;

    //inherited
    public final NumberPath<Byte> penalty;

    //inherited
    public final EnumPath<com.team5.projrental.entities.enums.DisputeReason> reason;

    // inherited
    public final QUser reportedUser;

    // inherited
    public final QUser reporter;

    //inherited
    public final EnumPath<com.team5.projrental.entities.enums.DisputeStatus> status;

    public QDisputeChatUser(String variable) {
        this(DisputeChatUser.class, forVariable(variable), INITS);
    }

    public QDisputeChatUser(Path<? extends DisputeChatUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDisputeChatUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDisputeChatUser(PathMetadata metadata, PathInits inits) {
        this(DisputeChatUser.class, metadata, inits);
    }

    public QDisputeChatUser(Class<? extends DisputeChatUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QDispute(type, metadata, inits);
        this.admin = _super.admin;
        this.chatUser = inits.isInitialized("chatUser") ? new QChatUser(forProperty("chatUser"), inits.get("chatUser")) : null;
        this.createdAt = _super.createdAt;
        this.details = _super.details;
        this.id = _super.id;
        this.penalty = _super.penalty;
        this.reason = _super.reason;
        this.reportedUser = _super.reportedUser;
        this.reporter = _super.reporter;
        this.status = _super.status;
    }

}

