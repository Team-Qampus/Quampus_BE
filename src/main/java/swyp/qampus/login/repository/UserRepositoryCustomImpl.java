package swyp.qampus.login.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import static swyp.qampus.login.entity.QUser.user;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public UserRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    @Modifying(clearAutomatically = true)
    public void resetMonthlyChoiceCnt() {
        queryFactory.update(user)
                .set(user.lastMonthChoiceCnt, user.monthlyChoiceCnt)
                .set(user.monthlyChoiceCnt, 0L)
                .execute();
    }

    @Override
    @Transactional
    @Modifying(clearAutomatically = true)
    public void resetWeeklyChoiceCnt() {
        queryFactory.update(user)
                .set(user.weeklyChoiceCnt, 0L)
                .execute();
    }

    @Override
    public int getThisMonthRankOfMajor(String major) {
        // Native Query
        String query = """
            select ranking from (
                select
                    u.major,
                    dense_rank() over (order by sum(u.monthly_choice_cnt) desc) as ranking
                from users as u
                group by u.major
            ) as ranked
            where ranked.major = :major
            """;

        Object result = em.createNativeQuery(query)
                .setParameter("major", major)
                .getSingleResult();

        return result != null ? ((Number) result).intValue() : 0;
    }

    @Override
    public int getLastMonthRankOfMajor(String major) {
        // Native Query
        String query = """
            select ranking from (
                select
                    u.major,
                    dense_rank() over (order by sum(u.last_month_choice_cnt) desc) as ranking
                from users as u
                group by u.major
            ) as ranked
            where ranked.major = :major
            """;

        Object result = em.createNativeQuery(query)
                .setParameter("major", major)
                .getSingleResult();

        return result != null ? ((Number) result).intValue() : 0;
    }
}
