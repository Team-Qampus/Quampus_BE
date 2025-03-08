package swyp.qampus.activity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.qampus.common.BaseEntity;
import swyp.qampus.university.domain.University;

@Entity
@NoArgsConstructor
@Getter
public class Activity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "activity_id",nullable = false)
    private Long activityId;

    @Column(name = "activity_type",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ActivityType activityType;

    @Column(name = "activity_major",nullable = false)
    private String activityMajor;


    @Column(name = "activity_detail_id",nullable = false)
    private Long activityDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    @Builder
    public Activity(ActivityType activityType,String activityMajor,Long activityDetailId,University university){
        this.activityDetailId=activityDetailId;
        this.activityMajor=activityMajor;
        this.activityType=activityType;
        this.university=university;
    }
}
