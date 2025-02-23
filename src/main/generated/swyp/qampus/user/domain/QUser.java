package swyp.qampus.user.domain;

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

    private static final long serialVersionUID = 882506620L;

    public static final QUser user = new QUser("user");

    public final ListPath<swyp.qampus.answer.domain.Answer, swyp.qampus.answer.domain.QAnswer> answers = this.<swyp.qampus.answer.domain.Answer, swyp.qampus.answer.domain.QAnswer>createList("answers", swyp.qampus.answer.domain.Answer.class, swyp.qampus.answer.domain.QAnswer.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final ListPath<swyp.qampus.like.domain.Like, swyp.qampus.like.domain.QLike> likeList = this.<swyp.qampus.like.domain.Like, swyp.qampus.like.domain.QLike>createList("likeList", swyp.qampus.like.domain.Like.class, swyp.qampus.like.domain.QLike.class, PathInits.DIRECT2);

    public final StringPath major = createString("major");

    public final DateTimePath<java.time.LocalDateTime> modifiedDate = createDateTime("modifiedDate", java.time.LocalDateTime.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final ListPath<swyp.qampus.question.domain.Question, swyp.qampus.question.domain.QQuestion> questions = this.<swyp.qampus.question.domain.Question, swyp.qampus.question.domain.QQuestion>createList("questions", swyp.qampus.question.domain.Question.class, swyp.qampus.question.domain.QQuestion.class, PathInits.DIRECT2);

    public final StringPath universityName = createString("universityName");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

