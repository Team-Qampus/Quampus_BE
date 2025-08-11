package swyp.qampus.login.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1506462969L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final ListPath<swyp.qampus.answer.domain.Answer, swyp.qampus.answer.domain.QAnswer> answers = this.<swyp.qampus.answer.domain.Answer, swyp.qampus.answer.domain.QAnswer>createList("answers", swyp.qampus.answer.domain.Answer.class, swyp.qampus.answer.domain.QAnswer.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final ListPath<swyp.qampus.curious.domain.Curious, swyp.qampus.curious.domain.QCurious> curiousList = this.<swyp.qampus.curious.domain.Curious, swyp.qampus.curious.domain.QCurious>createList("curiousList", swyp.qampus.curious.domain.Curious.class, swyp.qampus.curious.domain.QCurious.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final NumberPath<Long> lastMonthChoiceCnt = createNumber("lastMonthChoiceCnt", Long.class);

    public final ListPath<swyp.qampus.like.domain.Like, swyp.qampus.like.domain.QLike> likeList = this.<swyp.qampus.like.domain.Like, swyp.qampus.like.domain.QLike>createList("likeList", swyp.qampus.like.domain.Like.class, swyp.qampus.like.domain.QLike.class, PathInits.DIRECT2);

    public final StringPath major = createString("major");

    public final DateTimePath<java.time.LocalDateTime> modifiedDate = createDateTime("modifiedDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> monthlyChoiceCnt = createNumber("monthlyChoiceCnt", Long.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final ListPath<swyp.qampus.question.domain.Question, swyp.qampus.question.domain.QQuestion> questions = this.<swyp.qampus.question.domain.Question, swyp.qampus.question.domain.QQuestion>createList("questions", swyp.qampus.question.domain.Question.class, swyp.qampus.question.domain.QQuestion.class, PathInits.DIRECT2);

    public final swyp.qampus.university.domain.QUniversity university;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final NumberPath<Long> weeklyChoiceCnt = createNumber("weeklyChoiceCnt", Long.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.university = inits.isInitialized("university") ? new swyp.qampus.university.domain.QUniversity(forProperty("university")) : null;
    }

}

