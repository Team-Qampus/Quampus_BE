package swyp.qampus.question.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestion is a Querydsl query type for Question
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestion extends EntityPathBase<Question> {

    private static final long serialVersionUID = 850792498L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestion question = new QQuestion("question");

    public final NumberPath<Integer> answerCount = createNumber("answerCount", Integer.class);

    public final swyp.qampus.category.domain.QCategory category;

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> curiousCount = createNumber("curiousCount", Integer.class);

    public final ListPath<swyp.qampus.curious.domain.Curious, swyp.qampus.curious.domain.QCurious> curiousList = this.<swyp.qampus.curious.domain.Curious, swyp.qampus.curious.domain.QCurious>createList("curiousList", swyp.qampus.curious.domain.Curious.class, swyp.qampus.curious.domain.QCurious.class, PathInits.DIRECT2);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final DateTimePath<java.time.LocalDateTime> modifiedDate = createDateTime("modifiedDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> questionId = createNumber("questionId", Long.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> unreadAnswerCnt = createNumber("unreadAnswerCnt", Integer.class);

    public final swyp.qampus.login.entity.QUser user;

    public final NumberPath<Integer> viewCnt = createNumber("viewCnt", Integer.class);

    public QQuestion(String variable) {
        this(Question.class, forVariable(variable), INITS);
    }

    public QQuestion(Path<? extends Question> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestion(PathMetadata metadata, PathInits inits) {
        this(Question.class, metadata, inits);
    }

    public QQuestion(Class<? extends Question> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new swyp.qampus.category.domain.QCategory(forProperty("category")) : null;
        this.user = inits.isInitialized("user") ? new swyp.qampus.login.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

