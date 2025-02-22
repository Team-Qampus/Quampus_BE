package swyp.qampus.university.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.qampus.user.domain.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class University {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "university_id",nullable = false)
    private Long universityId;

    @Column(name = "university_name",nullable = false)
    private String universityName;

    @Column(name = "weekly_choice_cnt")
    private Integer weeklyChoiceCnt;

    @Column(name = "monthly_choice_cnt")
    private Integer monthlyChoiceCnt;

    //유저랑 양방향관계
    @OneToMany(mappedBy = "university")
    private List<User> users=new ArrayList<>();

    @Builder
    public University(String universityName){
        this.monthlyChoiceCnt=0;
        this.weeklyChoiceCnt=0;
        this.universityName=universityName;
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
}
