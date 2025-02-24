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
    private int viewCnt;

    @Builder
    public QuestionListResponseDto(Long questionId, String title, String category,
                                   String content, String universityName, LocalDateTime createdDate, int viewCnt) {
        this.question_id = questionId;
        this.title = title;
        this.category = category;
        this.content = content;
        this.universityName = universityName;
        this.createdDate = createdDate;
        this.viewCnt = viewCnt;
    }

    public static QuestionListResponseDto of(Question question) {
        return new QuestionListResponseDto(
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
