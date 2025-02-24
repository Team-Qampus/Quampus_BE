package swyp.qampus.question.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import swyp.qampus.category.domain.CategoryType;

import java.time.LocalDateTime;

@Getter
@Schema(name = "나의 질문목록 조회Dto")
public class MyQuestionResponseDto {
    private final Long questionId;
    private final String title;
    private final String content;
    private final CategoryType category;
    private final int viewCount;
    private final int answerCnt;
    private final int unreadAnswerCnt;
    private final LocalDateTime createdDate;

    @Builder
    private MyQuestionResponseDto(Long questionId, String title, String content, CategoryType category, int viewCount, int answerCnt, int unreadAnswerCnt, LocalDateTime createdDate) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.viewCount = viewCount;
        this.answerCnt = answerCnt;
        this.unreadAnswerCnt = unreadAnswerCnt;
        this.createdDate = createdDate;
    }

    public static MyQuestionResponseDto of(Question question) {
        return MyQuestionResponseDto.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .content(question.getContent())
                .category(question.getCategory().getCategoryName())
                .viewCount(question.getViewCnt())
                .answerCnt(question.getAnswerCount())
                .unreadAnswerCnt(question.getUnreadAnswerCnt())
                .createdDate(question.getCreateDate())
                .build();
    }
}