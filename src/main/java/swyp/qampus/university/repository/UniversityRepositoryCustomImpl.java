package swyp.qampus.university.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UniversityRepositoryCustomImpl implements UniversityRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<List<UniversityRankResponseDto>> getUniversityRanking(Integer limit, String period) {
        //eriod에 따라 주간/월간 선택
        String choiceColumn = period.equals("weekly") ? "weeklyChoiceCnt" : "monthlyChoiceCnt";

        //전체 선택 횟수
        Integer choiceCntAll = em.createQuery(
                "select coalesce(sum(univ." + choiceColumn + "), 0) from University as univ",
                Integer.class
        ).getSingleResult();

        String query="";

        if (period.equals("weekly")) {
             query =
                    "select new swyp.qampus.university.domain.response.UniversityRankResponseDto(" +
                            " univ.universityId," +
                            " univ.universityName," +
                            " rank() over (order by univ.weeklyChoiceCnt desc)," +
                            " size(univ.users)," +
                            " case when :choiceCntAll = 0 then 0 else (univ.weeklyChoiceCnt/:choiceCntAll)*100 end " +
                            ") " +
                            "from University as univ " +
                            "order by univ.weeklyChoiceCnt desc ";

        }

        if (period.equals("monthly")) {
             query =
                    "select new swyp.qampus.university.domain.response.UniversityRankResponseDto(" +
                            " univ.universityId," +
                            " univ.universityName," +
                            " rank() over (order by univ.monthlyChoiceCnt desc)," +
                            " size(univ.users)," +
                            " case when :choiceCntAll = 0 then 0 else (univ.monthlyChoiceCnt/:choiceCntAll)*100 end " +
                            ") " +
                            "from University as univ " +
                            "order by univ.monthlyChoiceCnt desc ";

        }

        if(limit == null){
            return Optional.of(
                    em.createQuery(query, UniversityRankResponseDto.class)
                            .setParameter("choiceCntAll", choiceCntAll)
                            .getResultList()
            );
        }else{
            return Optional.of(
                    em.createQuery(query, UniversityRankResponseDto.class)
                            .setParameter("choiceCntAll", choiceCntAll)
                            .setMaxResults(limit)
                            .getResultList()
            );
        }
    }
}
