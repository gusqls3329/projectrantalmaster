package com.team5.projrental.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDisputeUser is a Querydsl query type for DisputeUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDisputeUser extends EntityPathBase<DisputeUser> {

    private static final long serialVersionUID = 1498149324L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDisputeUser disputeUser = new QDisputeUser("disputeUser");

    public final QDispute _super;

    // inherited
    public final QAdmin admin;

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

    public final QUser user;

    public QDisputeUser(String variable) {
        this(DisputeUser.class, forVariable(variable), INITS);
    }

    public QDisputeUser(Path<? extends DisputeUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDisputeUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDisputeUser(PathMetadata metadata, PathInits inits) {
        this(DisputeUser.class, metadata, inits);
    }

    public QDisputeUser(Class<? extends DisputeUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QDispute(type, metadata, inits);
        this.admin = _super.admin;
        this.createdAt = _super.createdAt;
        this.details = _super.details;
        this.id = _super.id;
        this.penalty = _super.penalty;
        this.reason = _super.reason;
        this.reportedUser = _super.reportedUser;
        this.reporter = _super.reporter;
        this.status = _super.status;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

