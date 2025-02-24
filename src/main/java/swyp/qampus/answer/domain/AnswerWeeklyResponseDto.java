package swyp.qampus.answer.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerWeeklyResponseDto {
    private Long question_id;
    private Long answer_id;
    private String title;
    private String university_name;
    private String content;
    private int like_count;

    public AnswerWeeklyResponseDto(Answer answer) {
        this.question_id = answer.getQuestion().getQuestionId();
        this.answer_id = answer.getAnswerId();
        this.title = answer.getQuestion().getTitle();
        this.university_name = answer.getQuestion().getUser().getUniversity().getUniversityName();
        this.content = answer.getContent();
        this.like_count = answer.getLikeCnt();
    }
}