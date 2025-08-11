package swyp.qampus.curious.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCurious is a Querydsl query type for Curious
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCurious extends EntityPathBase<Curious> {

    private static final long serialVersionUID = -1468028242L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCurious curious = new QCurious("curious");

    public final swyp.qampus.common.QBaseEntity _super = new swyp.qampus.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> curiousId = createNumber("curiousId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final swyp.qampus.question.domain.QQuestion question;

    public final swyp.qampus.login.entity.QUser user;

    public QCurious(String variable) {
        this(Curious.class, forVariable(variable), INITS);
    }

    public QCurious(Path<? extends Curious> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCurious(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCurious(PathMetadata metadata, PathInits inits) {
        this(Curious.class, metadata, inits);
    }

    public QCurious(Class<? extends Curious> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.question = inits.isInitialized("question") ? new swyp.qampus.question.domain.QQuestion(forProperty("question"), inits.get("question")) : null;
        this.user = inits.isInitialized("user") ? new swyp.qampus.login.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

