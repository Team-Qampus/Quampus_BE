package swyp.qampus.university.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import swyp.qampus.answer.domain.QAnswer;
import swyp.qampus.login.entity.QUser;
import swyp.qampus.question.domain.QQuestion;
import swyp.qampus.university.domain.QUniversity;
import swyp.qampus.university.domain.response.QUniversityDetailResponseDto;
import swyp.qampus.university.domain.response.UniversityDetailResponseDto;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static swyp.qampus.answer.domain.QAnswer.*;
import static swyp.qampus.login.entity.QUser.*;
import static swyp.qampus.question.domain.QQuestion.*;
import static swyp.qampus.university.domain.QUniversity.*;


@Repository
public class UniversityRepositoryCustomImpl implements UniversityRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public UniversityRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<List<UniversityRankResponseDto>> getUniversityRanking(Integer limit, String period) {
        //eriod에 따라 주간/월간 선택
        String choiceColumn = period.equals("weekly") ? "weeklyChoiceCnt" : "monthlyChoiceCnt";

        //전체 선택 횟수
        Long choiceCntAll = em.createQuery(
                "select sum(univ." + choiceColumn + ") from University as univ",
                Long.class
        ).getSingleResult();


        String query = "";

        if (period.equals("weekly")) {
            query =
                    "select new swyp.qampus.university.domain.response.UniversityRankResponseDto(" +
                            "   univ.universityId," +
                            "   univ.universityName," +
                            "   cast(rank() over (order by univ.weeklyChoiceCnt desc) as integer)," +
                            "   cast(size(univ.users) as long)," +
                            "   case when :choiceCntAll = 0 then 0 else cast((univ.weeklyChoiceCnt * 100 / :choiceCntAll) as integer) end," +
                            "   cast(univ.weeklyChoiceCnt as long)," +
                            "   univ.latitude," + // 위도
                            "   univ.longitude" + // 경도
                            ") from University as univ " +
                            "order by univ.weeklyChoiceCnt desc";

        }

        if (period.equals("monthly")) {
            query =
                    "select new swyp.qampus.university.domain.response.UniversityRankResponseDto(" +
                            "   univ.universityId," +
                            "   univ.universityName," +
                            "   cast(rank() over (order by univ.monthlyChoiceCnt desc) as integer)," +
                            "   cast(size(univ.users) as long)," +
                            "   case when :choiceCntAll = 0 then 0 else cast((univ.monthlyChoiceCnt * 100 / :choiceCntAll) as integer) end," +
                            "   cast(univ.monthlyChoiceCnt as long)," +  // 채택 수
                            "   univ.latitude," + // 위도
                            "   univ.longitude" + // 경도
                            ") from University as univ " +
                            "order by univ.monthlyChoiceCnt desc";

        }

        if (limit == null) {
            return Optional.of(
                    em.createQuery(query, UniversityRankResponseDto.class)
                            .setParameter("choiceCntAll", choiceCntAll)
                            .getResultList()
            );
        } else {
            return Optional.of(
                    em.createQuery(query, UniversityRankResponseDto.class)
                            .setParameter("choiceCntAll", choiceCntAll)
                            .setMaxResults(limit)
                            .getResultList()
            );
        }
    }

    @Override
    public Optional<UniversityDetailResponseDto> getUniversityDetail(String universityName) {
        //전체 채택 수
        Long totalWeeklyChoiceCnt = queryFactory
                .select(university.weeklyChoiceCnt.sum())
                .from(university)
                .fetchOne();

        //랭크 연산 처리
        QUniversity subUniversity=new QUniversity("subUniversity");

        Expression<Long> rankExpression = JPAExpressions.select(subUniversity.count().add(1))
                .from(subUniversity)
                .where(subUniversity.weeklyChoiceCnt.gt(university.weeklyChoiceCnt));

        UniversityDetailResponseDto result =
        queryFactory.select(new QUniversityDetailResponseDto(
                        university.universityId,
                        university.universityName,
                        new CaseBuilder()
                                .when(university.monthlyChoiceCnt.eq(0L))
                                .then(0)
                                .otherwise(university.weeklyChoiceCnt.multiply(100).divide(totalWeeklyChoiceCnt).intValue()),
                        user.countDistinct(),
                        question.countDistinct(),
                        answer.countDistinct(),
                        university.weeklyChoiceCnt,
                        rankExpression
                ))
                .from(university)
                .leftJoin(university.users, user)
                .leftJoin(user.questions, question)
                .leftJoin(user.answers, answer)
                .where(university.universityName.eq(universityName))
                .groupBy(university.universityId)
                .fetchOne();


        return Optional.ofNullable(result);

    }

    /*
     *   벌크 연산 쿼리
     * */
    @Override
    @Transactional
    @Modifying(clearAutomatically = true)
    public void resetMonthlyChoiceCnt() {
        queryFactory.update(university)
                .set(university.lastMonthChoiceCnt, university.monthlyChoiceCnt)
                .set(university.monthlyChoiceCnt, 0L)
                .execute();
    }

    @Override
    @Transactional
    @Modifying(clearAutomatically = true)
    public void resetWeeklyChoiceCnt() {
        queryFactory.update(university)
                .set(university.weeklyChoiceCnt, 0L)
                .execute();
    }

    @Override
    public int getThisMonthRankOfSchool(String universityName) {
        //Native Query
        String query = """
                select ranking from (
                    select
                        u.university_name,
                        dense_rank() over (order by u.monthly_choice_cnt desc) as ranking
                    from university as u
                ) as ranked
                where ranked.university_name = :universityName
                LIMIT 1
                """;
        Object result = em.createNativeQuery(query)
                .setParameter("universityName", universityName)
                .getSingleResult();

        return result != null ? ((Number) result).intValue() : 0;
    }

    @Override
    public int getLastMonthRankOfSchool(String universityName) {
        //Native Query
        String query = """
                select ranking from (
                    select
                        u.university_name,
                        dense_rank() over (order by u.last_month_choice_cnt desc) as ranking
                    from university as u
                ) as ranked
                where ranked.university_name = :universityName
                LIMIT 1
                """;
        Object result = em.createNativeQuery(query)
                .setParameter("universityName", universityName)
                .getSingleResult();

        return result != null ? ((Number) result).intValue() : 0;
    }
}
