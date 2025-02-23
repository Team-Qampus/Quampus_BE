package swyp.qampus.answer.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import swyp.qampus.answer.domain.QAnswer;
import swyp.qampus.question.domain.QQuestion;
import swyp.qampus.question.domain.Question;

import static swyp.qampus.answer.domain.QAnswer.*;
import static swyp.qampus.question.domain.QQuestion.*;

@Repository
public class AnswerCustomRepositoryImpl implements AnswerCustomRepository {
    private final JPAQueryFactory queryFactory;

    public AnswerCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Integer countChoiceOfAnswer(Long questionId) {
        Long count=  queryFactory
                .select(answer.count())
                .from(question)
                .leftJoin(answer)
                .on(questionIdEq(questionId))
                .where(answer.isChosen.isTrue())
                .fetchOne();
        return count !=null ? count.intValue() : 0 ;
    }

    BooleanExpression answerIdEq(Long answerId){
        return answerId ==null ? null : answer.answerId.eq(answerId);
    }

    BooleanExpression questionIdEq(Long questionId){
        return answer.question.questionId.eq(questionId);
    }
}
