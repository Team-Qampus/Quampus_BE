package swyp.qampus.answer.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.qampus.question.domain.Question;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AnswerListResponseDto {
    private Long question_id;
    private String title;
    private String category;
    private String content;
    private String universityName;
    private LocalDateTime createdDate;
    private int viewCnt;

    public AnswerListResponseDto(Question question) {
        this.question_id = question.getQuestionId();
        this.title = question.getTitle();
        this.category = question.getCategory().getCategoryName();
        this.content = question.getContent();
        this.universityName = question.getUser().getUniversityName();
        this.createdDate = question.getCreateDate();
        this.viewCnt = question.getViewCnt();
    }
}
