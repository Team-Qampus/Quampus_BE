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

    @Builder
    public QuestionResponseDto(Long questionId, String title, String category,
                               String content, String universityName, LocalDateTime createdDate, int viewCnt) {
        this.questionId = questionId;
        this.title = title;
        this.category = category;
        this.content = content;
        this.universityName = universityName;
        this.createdDate = createdDate;
        this.viewCnt = viewCnt;
    }

    public static QuestionResponseDto of(Question question) {
        return new QuestionResponseDto(
                question.getQuestionId(),
                question.getTitle(),
                question.getCategory().getCategoryName(),
                question.getContent(),
                question.getUser().getUniversityName(),
                question.getCreateDate(),
                question.getViewCnt()
        );
    }
}
