package com.team5.projrental.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDisputeProduct is a Querydsl query type for DisputeProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDisputeProduct extends EntityPathBase<DisputeProduct> {

    private static final long serialVersionUID = -2095875250L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDisputeProduct disputeProduct = new QDisputeProduct("disputeProduct");

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

    public final QProduct product;

    //inherited
    public final EnumPath<com.team5.projrental.entities.enums.DisputeReason> reason;

    // inherited
    public final QUser reportedUser;

    // inherited
    public final QUser reporter;

    //inherited
    public final EnumPath<com.team5.projrental.entities.enums.DisputeStatus> status;

    public QDisputeProduct(String variable) {
        this(DisputeProduct.class, forVariable(variable), INITS);
    }

    public QDisputeProduct(Path<? extends DisputeProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDisputeProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDisputeProduct(PathMetadata metadata, PathInits inits) {
        this(DisputeProduct.class, metadata, inits);
    }

    public QDisputeProduct(Class<? extends DisputeProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QDispute(type, metadata, inits);
        this.admin = _super.admin;
        this.createdAt = _super.createdAt;
        this.details = _super.details;
        this.id = _super.id;
        this.penalty = _super.penalty;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
        this.reason = _super.reason;
        this.reportedUser = _super.reportedUser;
        this.reporter = _super.reporter;
        this.status = _super.status;
    }

}

