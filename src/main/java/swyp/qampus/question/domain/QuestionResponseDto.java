package swyp.qampus.question.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QuestionResponseDto {
    private Long questionId;
    private String title;
    private String category;
    private String content;
    private String universityName;
    private LocalDateTime createdDate;
    private int viewCnt;

    public QuestionResponseDto(Question question) {
        this.questionId = question.getQuestionId();
        this.title = question.getTitle();
        this.category = question.getCategory().getCategoryName();
        this.content = question.getContent();
        this.universityName = question.getUser().getUniversityName();
        this.createdDate = question.getCreateDate();
        this.viewCnt = question.getViewCnt();
    }
}
