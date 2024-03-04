package com.team5.projrental.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDisputePayment is a Querydsl query type for DisputePayment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDisputePayment extends EntityPathBase<DisputePayment> {

    private static final long serialVersionUID = 1721884773L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDisputePayment disputePayment = new QDisputePayment("disputePayment");

    public final QDispute _super;

    // inherited
    public final QAdmin admin;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt;

    //inherited
    public final StringPath details;

    //inherited
    public final NumberPath<Long> id;

    public final QPaymentInfo paymentInfo;

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

    public QDisputePayment(String variable) {
        this(DisputePayment.class, forVariable(variable), INITS);
    }

    public QDisputePayment(Path<? extends DisputePayment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDisputePayment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDisputePayment(PathMetadata metadata, PathInits inits) {
        this(DisputePayment.class, metadata, inits);
    }

    public QDisputePayment(Class<? extends DisputePayment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QDispute(type, metadata, inits);
        this.admin = _super.admin;
        this.createdAt = _super.createdAt;
        this.details = _super.details;
        this.id = _super.id;
        this.paymentInfo = inits.isInitialized("paymentInfo") ? new QPaymentInfo(forProperty("paymentInfo"), inits.get("paymentInfo")) : null;
        this.penalty = _super.penalty;
        this.reason = _super.reason;
        this.reportedUser = _super.reportedUser;
        this.reporter = _super.reporter;
        this.status = _super.status;
    }

}

