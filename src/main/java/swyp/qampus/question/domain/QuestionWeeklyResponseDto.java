package swyp.qampus.question.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionWeeklyResponseDto {
    private Long question_id;
    private String title;
    private String university_name;
    private String major;
    private int curious_count;
    private int view_count;
    private int total_score;

    public QuestionWeeklyResponseDto(Question question) {
        this.question_id = question.getQuestionId();
        this.title = question.getTitle();
        this.university_name = question.getUser().getUniversityName();
        this.major = question.getUser().getMajor();
        this.curious_count = question.getCuriousCount();
        this.view_count = question.getViewCnt();
        this.total_score = question.getCuriousCount() + question.getViewCnt();
    }
}