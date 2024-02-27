package com.team5.projrental.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatBot is a Querydsl query type for ChatBot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatBot extends EntityPathBase<ChatBot> {

    private static final long serialVersionUID = -2129002260L;

    public static final QChatBot chatBot = new QChatBot("chatBot");

    public final com.team5.projrental.entities.mappedsuper.QBaseAt _super = new com.team5.projrental.entities.mappedsuper.QBaseAt(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> depth = createNumber("depth", Integer.class);

    public final NumberPath<Integer> grp = createNumber("grp", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    public final StringPath mention = createString("mention");

    public final StringPath redirectUrl = createString("redirectUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QChatBot(String variable) {
        super(ChatBot.class, forVariable(variable));
    }

    public QChatBot(Path<? extends ChatBot> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatBot(PathMetadata metadata) {
        super(ChatBot.class, metadata);
    }

}

