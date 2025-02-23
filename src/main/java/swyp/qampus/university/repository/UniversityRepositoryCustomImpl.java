package swyp.qampus.university.repository;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestHeader;
import swyp.qampus.university.domain.QUniversity;
import swyp.qampus.university.domain.response.QUniversityRankResponseDto;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static swyp.qampus.university.domain.QUniversity.*;

@Repository
@RequiredArgsConstructor
public class UniversityRepositoryCustomImpl implements UniversityRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<List<UniversityRankResponseDto>> getUniversityRanking(Integer limit,String period) {
        //채택하기 합계
        Long choiceCntAll= Objects.requireNonNullElse(
                queryFactory
                        .select(university.monthlyChoiceCnt.count())
                        .from(university)
                        .fetchOne(),
                0L
        );

        if(period.equals("weekly")){
            String query=
                    "select new swyp.qampus.university.domain.response.UniversityRankResponseDto(" +
                            " univ.universityId," +
                            " univ.universityName," +
                            " rank() over (order by univ.weeklyChoiceCnt desc)," +
                            " size(univ.users)," +
                            " case when :choiceCntAll = 0 then 0 else (univ.weeklyChoiceCnt/:choiceCntAll)*100 end " +
                            ") "+
                            "from University as univ " +
                            "order by univ.weeklyChoiceCnt desc ";

            return Optional.of(
                    em.createQuery(query,UniversityRankResponseDto.class)
                            .setParameter("choiceCntAll",choiceCntAll)
                            .setMaxResults(limit)
                            .getResultList()
            );
        }

        if(period.equals("monthly")){
            String query=
                    "select new swyp.qampus.university.domain.response.UniversityRankResponseDto(" +
                            " univ.universityId," +
                            " univ.universityName," +
                            " rank() over (order by univ.monthlyChoiceCnt desc)," +
                            " size(univ.users)," +
                            " case when :choiceCntAll = 0 then 0 else (univ.monthlyChoiceCnt/:choiceCntAll)*100 end " +
                            ") "+
                            "from University as univ " +
                            "order by univ.monthlyChoiceCnt desc ";

            return Optional.of(
                    em.createQuery(query,UniversityRankResponseDto.class)
                            .setParameter("choiceCntAll",choiceCntAll)
                            .setMaxResults(limit)
                            .getResultList()
            );
        }

        return Optional.empty();
    }
}
