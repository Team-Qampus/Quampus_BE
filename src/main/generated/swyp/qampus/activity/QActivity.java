package swyp.qampus.activity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QActivity is a Querydsl query type for Activity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QActivity extends EntityPathBase<Activity> {

    private static final long serialVersionUID = 1348800036L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QActivity activity = new QActivity("activity");

    public final swyp.qampus.common.QBaseEntity _super = new swyp.qampus.common.QBaseEntity(this);

    public final NumberPath<Long> activityDetailId = createNumber("activityDetailId", Long.class);

    public final NumberPath<Long> activityId = createNumber("activityId", Long.class);

    public final StringPath activityMajor = createString("activityMajor");

    public final EnumPath<ActivityType> activityType = createEnum("activityType", ActivityType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final swyp.qampus.university.domain.QUniversity university;

    public QActivity(String variable) {
        this(Activity.class, forVariable(variable), INITS);
    }

    public QActivity(Path<? extends Activity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QActivity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QActivity(PathMetadata metadata, PathInits inits) {
        this(Activity.class, metadata, inits);
    }

    public QActivity(Class<? extends Activity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.university = inits.isInitialized("university") ? new swyp.qampus.university.domain.QUniversity(forProperty("university")) : null;
    }

}

