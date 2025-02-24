package swyp.qampus.question.domain;

import lombok.Builder;
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

    @Builder
    private QuestionWeeklyResponseDto(Long question_id, String title, String university_name, String major, int curious_count, int view_count, int total_score) {
        this.question_id = question_id;
        this.title = title;
        this.university_name = university_name;
        this.major = major;
        this.curious_count = curious_count;
        this.view_count = view_count;
        this.total_score = total_score;
    }

    public static QuestionWeeklyResponseDto of(Question question) {
        return QuestionWeeklyResponseDto.builder()
                .question_id(question.getQuestionId())
                .title(question.getTitle())
                .university_name(question.getUser().getUniversityName())
                .major(question.getUser().getMajor())
                .curious_count(question.getCuriousCount())
                .view_count(question.getViewCnt())
                .total_score(question.getCuriousCount() + question.getViewCnt())
                .build();
    }
}