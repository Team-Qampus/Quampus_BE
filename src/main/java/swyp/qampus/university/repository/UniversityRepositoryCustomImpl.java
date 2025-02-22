package swyp.qampus.university.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class UniversityRepositoryCustomImpl implements UniversityRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public UniversityRepositoryCustomImpl(EntityManager em){this.queryFactory=new JPAQueryFactory(em);}


}
