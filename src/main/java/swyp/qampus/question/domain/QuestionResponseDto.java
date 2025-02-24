package swyp.qampus.question.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import swyp.qampus.category.domain.CategoryType;

import java.time.LocalDateTime;

@Getter
@Schema(description = "질문 상세보기 Dto입니다.")
public class QuestionResponseDto {
    private Long questionId;
    private String title;
    private CategoryType category;
    private String content;
    private String universityName;
    private LocalDateTime createdDate;
    private int viewCnt;

    @Builder
    public QuestionResponseDto(Long questionId, String title, CategoryType category,
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
                question.getUser().getUniversity().getUniversityName(),
                question.getCreateDate(),
                question.getViewCnt()
        );
    }

}
