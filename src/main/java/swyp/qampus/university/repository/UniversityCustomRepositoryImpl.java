package swyp.qampus.university.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import swyp.qampus.university.domain.response.QUniversityRankResponseDto;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;
import swyp.qampus.user.domain.QUser;

import java.util.List;

@Repository
public class UniversityCustomRepositoryImpl implements UniversityCustomRepository {
    private final JPAQueryFactory queryFactory;

    public UniversityCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<UniversityRankResponseDto> getUniversityList(String universityName,int limit) {
        return null;/*QUniversityRankResponseDto(
                queryFactory
                        .select(QUser.user.universityName)
                        .from(QUser.user)
                        .join(An)
        );*/
    }
}
