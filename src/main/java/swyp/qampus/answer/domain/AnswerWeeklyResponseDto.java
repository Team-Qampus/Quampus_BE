package swyp.qampus.answer.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "주별 답변 목록")
public class AnswerWeeklyResponseDto {
    @Schema(description = "질문 ID", example = "1")
    private Long question_id;

    @Schema(description = "답변 ID", example = "10")
    private Long answer_id;

    @Schema(description = "답변 제목", example = "이 답변의 제목입니다.")
    private String title;

    @Schema(description = "대학교명", example = "서울대학교")
    private String university_name;

    @Schema(description = "답변 내용", example = "이 질문에 대한 답변입니다.")
    private String content;

    @Schema(description = "좋아요 수", example = "25")
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