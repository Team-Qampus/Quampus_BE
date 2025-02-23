package swyp.qampus.question.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class QuestionListResponseDto {
    private Long question_id;
    private String title;
    private String category;
    private String content;
    private String universityName;
    private LocalDateTime createdDate;
    private int answerCount;

    public QuestionListResponseDto(Question question) {
        this.question_id = question.getQuestionId();
        this.title = question.getTitle();
        this.category = question.getCategory().getCategoryName();
        this.content = question.getContent();
        this.universityName = question.getUser().getUniversityName();
        this.createdDate = question.getCreateDate();
        this.answerCount = question.getAnswerCount();
    }
}
