package swyp.qampus.answer.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
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

    @Builder
    private AnswerWeeklyResponseDto(Long question_id, Long answer_id, String title, String university_name, String content, int like_count) {
        this.question_id = question_id;
        this.answer_id = answer_id;
        this.title = title;
        this.university_name = university_name;
        this.content = content;
        this.like_count = like_count;
    }

    public static AnswerWeeklyResponseDto of(Answer answer) {
        return AnswerWeeklyResponseDto.builder()
                .question_id(answer.getQuestion().getQuestionId())
                .answer_id(answer.getAnswerId())
                .title(answer.getQuestion().getTitle())
                .university_name(answer.getQuestion().getUser().getUniversity().getUniversityName())
                .content(answer.getContent())
                .like_count(answer.getLikeCnt())
                .build();
    }
}
