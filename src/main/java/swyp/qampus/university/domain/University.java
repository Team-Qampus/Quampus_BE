package swyp.qampus.university.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import swyp.qampus.activity.Activity;
import swyp.qampus.common.BaseEntity;
import swyp.qampus.login.entity.User;


import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class University extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "university_id",nullable = false)
    private Long universityId;

    @Column(name = "university_name",nullable = false)
    private String universityName;

    @Column(name = "weekly_choice_cnt")
    private Long weeklyChoiceCnt;

    @Column(name = "monthly_choice_cnt")
    private Long monthlyChoiceCnt;

    @Column(name = "last_month_choice_cnt")
    private Long lastMonthChoiceCnt;

    //유저랑 양방향관계
    @OneToMany(mappedBy = "university")
    private List<User> users=new ArrayList<>();

    @OneToMany(mappedBy = "university")
    private List<Activity> activities = new ArrayList<>();

    //위도
    @Column(name = "latitude")
    private Double latitude;

    //경도
    @Column(name = "longitude")
    private Double longitude;

    @Builder
    public University(String universityName){
        this.weeklyChoiceCnt=0L;
        this.monthlyChoiceCnt=0L;
        this.lastMonthChoiceCnt=0L;
        this.universityName=universityName;
    }

    //유저 추가
    public void addUser(User user){
        this.users.add(user);
    }
    //채택 수 증가
    public void increaseChoiceCnt(){
        this.monthlyChoiceCnt++;
        this.weeklyChoiceCnt++;
    }

    //채택 수 감소
    public void decreaseChoiceCnt(){
        if(weeklyChoiceCnt>0){
            weeklyChoiceCnt--;
        }
        if(monthlyChoiceCnt>0){
            monthlyChoiceCnt--;
        }
    }

//    Test을 위해 임시적 setter 사용
    public void setMonthlyChoiceCnt(Long choiceCnt){
        this.monthlyChoiceCnt=choiceCnt;
    }

    public void setLastMonthChoiceCnt(Long choiceCnt){
        this.lastMonthChoiceCnt=choiceCnt;
    }
}
