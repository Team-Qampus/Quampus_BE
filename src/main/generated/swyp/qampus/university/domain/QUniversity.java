package swyp.qampus.university.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUniversity is a Querydsl query type for University
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUniversity extends EntityPathBase<University> {

    private static final long serialVersionUID = -1972902398L;

    public static final QUniversity university = new QUniversity("university");

    public final swyp.qampus.common.QBaseEntity _super = new swyp.qampus.common.QBaseEntity(this);

    public final ListPath<swyp.qampus.activity.Activity, swyp.qampus.activity.QActivity> activities = this.<swyp.qampus.activity.Activity, swyp.qampus.activity.QActivity>createList("activities", swyp.qampus.activity.Activity.class, swyp.qampus.activity.QActivity.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> lastMonthChoiceCnt = createNumber("lastMonthChoiceCnt", Long.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Long> monthlyChoiceCnt = createNumber("monthlyChoiceCnt", Long.class);

    public final NumberPath<Long> universityId = createNumber("universityId", Long.class);

    public final StringPath universityName = createString("universityName");

    public final ListPath<swyp.qampus.login.entity.User, swyp.qampus.login.entity.QUser> users = this.<swyp.qampus.login.entity.User, swyp.qampus.login.entity.QUser>createList("users", swyp.qampus.login.entity.User.class, swyp.qampus.login.entity.QUser.class, PathInits.DIRECT2);

    public final NumberPath<Long> weeklyChoiceCnt = createNumber("weeklyChoiceCnt", Long.class);

    public QUniversity(String variable) {
        super(University.class, forVariable(variable));
    }

    public QUniversity(Path<? extends University> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUniversity(PathMetadata metadata) {
        super(University.class, metadata);
    }

}

