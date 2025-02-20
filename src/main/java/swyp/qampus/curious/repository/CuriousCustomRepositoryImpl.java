package swyp.qampus.curious.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import swyp.qampus.curious.domain.Curious;
import java.util.Optional;
import static swyp.qampus.curious.domain.QCurious.*;
import static swyp.qampus.question.domain.QQuestion.*;
import static swyp.qampus.user.domain.QUser.*;

public class CuriousCustomRepositoryImpl implements CuriousCustomRepository {
    private final JPAQueryFactory queryFactory;

    public CuriousCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Curious> findCuriousByQuestionAndUser(Long questionId, String userId) {
        return Optional.ofNullable(
                queryFactory.select(curious)
                        .from(curious)
                        .join(curious.user, user)
                        .where(userAndQuestionIdEq(userId, questionId))
                        .fetchOne()
        );
    }

    private BooleanExpression userAndQuestionIdEq(String userId, Long questionId) {
        if (userIdEq(userId) == null) {
            return questionIdEq(questionId);
        }
        if (questionIdEq(questionId) == null) {
            return userIdEq(userId);
        }
        return questionIdEq(questionId).and(userIdEq(userId));
    }

    private BooleanExpression questionIdEq(Long questionId) {
        return (questionId == null) ? null : question.questionId.eq(questionId);
    }

    private BooleanExpression userIdEq(String userId) {
        return userId == null ? null : user.userId.eq(userId);
    }
}
