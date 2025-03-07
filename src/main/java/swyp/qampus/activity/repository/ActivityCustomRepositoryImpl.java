package swyp.qampus.activity.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import swyp.qampus.activity.QActivity;
import swyp.qampus.activity.dto.QRecentActivityResponseDto;
import swyp.qampus.activity.dto.RecentActivityResponseDto;
import swyp.qampus.university.domain.QUniversity;

import java.util.Collections;
import java.util.List;

import static swyp.qampus.activity.QActivity.*;
import static swyp.qampus.university.domain.QUniversity.*;

public class ActivityCustomRepositoryImpl implements ActivityCustomRepository{
    private final JPAQueryFactory queryFactory;

    public ActivityCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    //특정 개수만큼 최신 순으로 활동 가져오기
    @Override
    public List<RecentActivityResponseDto> getRecentActivityOrderByRecent(Long activityId, Integer count, Long universityId) {
        List<RecentActivityResponseDto> recentActivityResponseDtoList=queryFactory
                .select(new QRecentActivityResponseDto(
                        activity.activityMajor,
                        activity.activityType,
                        activity.activityDetailId,
                        activity.activityId
                ))
                .from(activity)
                .innerJoin(activity.university, university)
                .where(totalCondition(activityId,universityId))
                .orderBy(activity.activityId.desc())
                .limit(count)
                .fetch();

        return recentActivityResponseDtoList.isEmpty() ? Collections.emptyList() : recentActivityResponseDtoList;
    }

    BooleanExpression activityIdGt(Long activityId){
        return activityId == null ? null : activity.activityId.gt(activityId);
    }

    BooleanExpression universityIdEq(Long universityId){
        return universityId == null ? null : university.universityId.eq(universityId);
    }

    BooleanExpression totalCondition(Long activityId,Long universityId){
        BooleanExpression activityIdCondition=activityIdGt(activityId);
        BooleanExpression universityIdCondition=universityIdEq(universityId);

        if(activityIdCondition==null) return universityIdCondition;
        if(universityIdCondition==null) return activityIdCondition;

        return activityIdCondition.and(universityIdCondition);
    }

}
