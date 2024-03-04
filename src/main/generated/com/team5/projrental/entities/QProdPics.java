package com.team5.projrental.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProdPics is a Querydsl query type for ProdPics
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProdPics extends EntityPathBase<ProdPics> {

    private static final long serialVersionUID = 278682147L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProdPics prodPics = new QProdPics("prodPics");

    public final com.team5.projrental.entities.mappedsuper.QCreatedAt _super = new com.team5.projrental.entities.mappedsuper.QCreatedAt(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QProduct product;

    public final StringPath storedPic = createString("storedPic");

    public QProdPics(String variable) {
        this(ProdPics.class, forVariable(variable), INITS);
    }

    public QProdPics(Path<? extends ProdPics> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProdPics(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProdPics(PathMetadata metadata, PathInits inits) {
        this(ProdPics.class, metadata, inits);
    }

    public QProdPics(Class<? extends ProdPics> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

