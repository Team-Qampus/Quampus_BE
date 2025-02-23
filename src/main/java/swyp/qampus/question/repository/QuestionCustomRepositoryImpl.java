package swyp.qampus.question.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.question.domain.Question;

import java.util.List;

import static swyp.qampus.question.domain.QQuestion.*;

@Repository
public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {

    private final JPAQueryFactory queryFactory;

    public QuestionCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Question> findByCategoryId(Long categoryId, int page, int size, String sort) {
        return queryFactory
                .selectFrom(question)
                .where(categoryIdEq(categoryId))
                .orderBy(getSortOrder(sort))
                .offset((long) (page - 1) * size)
                .limit(size)
                .fetch();
    }

    @Override
    public List<Question> findAllPaged(int page, int size, String sort) {
        return queryFactory
                .selectFrom(question)
                .orderBy(getSortOrder(sort))
                .offset((long) (page - 1) * size)
                .limit(size)
                .fetch();
    }

    @Override
    public List<Question> searchByKeyword(String value, String sort, int page, int size) {
        return queryFactory
                .selectFrom(question)
                .where(titleOrContentContains(value))
                .orderBy(getSortOrder(sort))
                .offset((long) (page - 1) * size)
                .limit(size)
                .fetch();
    }

    @Override
    public List<Question> findWeeklyPopularQuestions() {
        return queryFactory
                .selectFrom(question)
                .orderBy(question.curiousCount.add(question.viewCnt).desc()) //(curiousCount + viewCnt) 기준 정렬
                .limit(3) //상위 3개만 조회
                .fetch();
    }

    @Override
    public List<Question> findMyQuestions(Long userId, Long categoryId, String sort, int page, int size) {
        return queryFactory
                .selectFrom(question)
                .where(userIdEq(userId), categoryIdEq(categoryId))
                .orderBy(getSortOrder(sort))
                .offset((long) (page - 1) * size)
                .limit(size)
                .fetch();
    }

    private OrderSpecifier<?> getSortOrder(String sort) {
        if ("popular".equals(sort)) {
            return question.viewCnt.desc(); // 인기순 (조회수 내림차순)
        } else if ("latest".equals(sort)) {
            return question.createDate.desc(); // 최신순 (생성일 내림차순)
        } else {
            return question.createDate.desc(); // 기본값을 최신순으로 설정
        }
    }

    private BooleanExpression titleOrContentContains(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return question.title.containsIgnoreCase(value)
                .or(question.content.containsIgnoreCase(value));
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? question.user.userId.eq(userId) : null;
    }

    private BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? question.category.categoryId.eq(categoryId) : null;
    }
}
