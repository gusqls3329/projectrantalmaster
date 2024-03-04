package com.team5.projrental.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDisputeBoard is a Querydsl query type for DisputeBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDisputeBoard extends EntityPathBase<DisputeBoard> {

    private static final long serialVersionUID = -819681019L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDisputeBoard disputeBoard = new QDisputeBoard("disputeBoard");

    public final QDispute _super;

    // inherited
    public final QAdmin admin;

    public final QBoard board;

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

    public QDisputeBoard(String variable) {
        this(DisputeBoard.class, forVariable(variable), INITS);
    }

    public QDisputeBoard(Path<? extends DisputeBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDisputeBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDisputeBoard(PathMetadata metadata, PathInits inits) {
        this(DisputeBoard.class, metadata, inits);
    }

    public QDisputeBoard(Class<? extends DisputeBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QDispute(type, metadata, inits);
        this.admin = _super.admin;
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
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

