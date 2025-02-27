package swyp.qampus.ai.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAi is a Querydsl query type for Ai
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAi extends EntityPathBase<Ai> {

    private static final long serialVersionUID = 541068982L;

    public static final QAi ai = new QAi("ai");

    public final swyp.qampus.common.QBaseEntity _super = new swyp.qampus.common.QBaseEntity(this);

    public final NumberPath<Long> aiId = createNumber("aiId", Long.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public QAi(String variable) {
        super(Ai.class, forVariable(variable));
    }

    public QAi(Path<? extends Ai> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAi(PathMetadata metadata) {
        super(Ai.class, metadata);
    }

}

