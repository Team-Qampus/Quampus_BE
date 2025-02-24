package swyp.qampus.answer.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAnswer is a Querydsl query type for Answer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnswer extends EntityPathBase<Answer> {

    private static final long serialVersionUID = 461982562L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAnswer answer = new QAnswer("answer");

    public final NumberPath<Long> answerId = createNumber("answerId", Long.class);

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final ListPath<swyp.qampus.image.domain.Image, swyp.qampus.image.domain.QImage> images = this.<swyp.qampus.image.domain.Image, swyp.qampus.image.domain.QImage>createList("images", swyp.qampus.image.domain.Image.class, swyp.qampus.image.domain.QImage.class, PathInits.DIRECT2);

    public final BooleanPath isChosen = createBoolean("isChosen");

    public final NumberPath<Integer> likeCnt = createNumber("likeCnt", Integer.class);

    public final ListPath<swyp.qampus.like.domain.Like, swyp.qampus.like.domain.QLike> likeList = this.<swyp.qampus.like.domain.Like, swyp.qampus.like.domain.QLike>createList("likeList", swyp.qampus.like.domain.Like.class, swyp.qampus.like.domain.QLike.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> modifiedDate = createDateTime("modifiedDate", java.time.LocalDateTime.class);

    public final swyp.qampus.question.domain.QQuestion question;

    public final swyp.qampus.user.domain.QUser user;

    public QAnswer(String variable) {
        this(Answer.class, forVariable(variable), INITS);
    }

    public QAnswer(Path<? extends Answer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAnswer(PathMetadata metadata, PathInits inits) {
        this(Answer.class, metadata, inits);
    }

    public QAnswer(Class<? extends Answer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.question = inits.isInitialized("question") ? new swyp.qampus.question.domain.QQuestion(forProperty("question"), inits.get("question")) : null;
        this.user = inits.isInitialized("user") ? new swyp.qampus.user.domain.QUser(forProperty("user")) : null;
    }

}

