package swyp.qampus.curious.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import swyp.qampus.curious.domain.Curious;
import swyp.qampus.curious.repository.CuriousCustomRepository;

import java.util.Optional;
import static swyp.qampus.curious.domain.QCurious.*;
import static swyp.qampus.login.entity.QUser.user;
import static swyp.qampus.question.domain.QQuestion.*;

@Repository
public class CuriousCustomRepositoryImpl implements CuriousCustomRepository {
    private final JPAQueryFactory queryFactory;

    public CuriousCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Curious> find_CuriousCustomByQuestionAndUser(Long questionId, Long userId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(curious)
                        .leftJoin(curious.user, user)
                        .where(userAndQuestionIdEq(userId, questionId))
                        .fetchOne()
        );
    }

    private BooleanExpression userAndQuestionIdEq(Long userId, Long questionId) {
        BooleanExpression userCondition = userIdEq(userId);
        BooleanExpression questionCondition = questionIdEq(questionId);

        if (userCondition == null && questionCondition == null) {
            return null;
        }
        if (userCondition == null) {
            return questionCondition;
        }
        if (questionCondition == null) {
            return userCondition;
        }
        return userCondition.and(questionCondition);
    }


    private BooleanExpression questionIdEq(Long questionId) {
        return (questionId == null) ? null : curious.question.questionId.eq(questionId);
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId == null ? null : curious.user.userId.eq(userId);
    }
}
