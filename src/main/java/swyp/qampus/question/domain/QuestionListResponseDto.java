package swyp.qampus.question.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.qampus.category.domain.CategoryType;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(description = "질문 목록 Dto")
public class QuestionListResponseDto {
    private Long question_id;
    private String title;
    private CategoryType category;
    private String content;
    private String universityName;
    private LocalDateTime createdDate;
    private int viewCnt;

    @Builder
    public QuestionListResponseDto(Long questionId, String title, CategoryType category,
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
                question.getUser().getUniversity().getUniversityName(),
                question.getCreateDate(),
                question.getViewCnt()
        );
    }
}
