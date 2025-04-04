package swyp.qampus.question.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import swyp.qampus.login.entity.User;
import swyp.qampus.question.domain.Question;

import java.util.List;
import java.util.Objects;

import static swyp.qampus.question.domain.QQuestion.*;

@Repository
public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {

    private final JPAQueryFactory queryFactory;

    public QuestionCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Question> findByCategoryId(Long categoryId, Pageable pageable, String sort) {
        List<Question> questions = queryFactory
                .selectFrom(question)
                .where(categoryFilter(categoryId))
                .orderBy(getSortOrder(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(question)
                .where(categoryFilter(categoryId))
                .fetchCount();  // 전체 데이터 개수 조회

        return new PageImpl<>(questions, pageable, total);
    }

    @Override
    public Page<Question> searchByKeyword(String value, String sort, Pageable pageable) {
        List<Question> questions = queryFactory
                .selectFrom(question)
                .where(titleOrContentContains(value))
                .orderBy(getSortOrder(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(question)
                .where(titleOrContentContains(value))
                .fetchCount();  // 전체 데이터 개수 조회

        return new PageImpl<>(questions, pageable, total);
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
    public Page<Question> findMyQuestions(User user, Long categoryId, String sort, Pageable pageable) {
        List<Question> questions = queryFactory
                .selectFrom(question)
                .where(userIdEq(user), categoryFilter(categoryId))
                .orderBy(getSortOrder(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(question)
                .where(userIdEq(user), categoryFilter(categoryId))
                .fetchCount();  // 전체 데이터 개수 조회

        return new PageImpl<>(questions, pageable, total);
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

    private BooleanExpression userIdEq(User user) {
        return user != null ? question.user.eq(user) : null;
    }

    private BooleanExpression categoryFilter(Long categoryId) {
        return Objects.equals(categoryId, 1L) ? null : question.category.categoryId.eq(categoryId);
    }
}
