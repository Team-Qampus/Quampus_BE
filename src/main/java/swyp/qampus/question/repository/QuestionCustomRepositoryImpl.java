package swyp.qampus.question.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
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
    public List<Question> findByCategoryId(Long categoryId, Pageable pageable, String sort) {
        return queryFactory
                .selectFrom(question)
                .where(question.category.categoryId.eq(categoryId))
                .orderBy(getSortOrder(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Question> findAllPaged(Pageable pageable, String sort) {
        return queryFactory
                .selectFrom(question)
                .orderBy(getSortOrder(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Question> searchByKeyword(String value, String sort, Pageable pageable) {
        return queryFactory
                .selectFrom(question)
                .where(titleOrContentContains(value))
                .orderBy(getSortOrder(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private OrderSpecifier<?> getSortOrder(String sort) {
        if ("popular".equals(sort)) {
            return question.curiousCount.add(question.viewCnt).desc(); // 인기순 (조회수 내림차순)
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
}
