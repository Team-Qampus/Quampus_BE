package swyp.qampus.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import swyp.qampus.answer.domain.AnswerWeeklyResponseDto;
import swyp.qampus.question.domain.QuestionWeeklyResponseDto;

import java.util.List;

@Getter
@AllArgsConstructor
public class HomeResponseDto {
    @Schema(description = "주별 인기 질문")
    private List<QuestionWeeklyResponseDto> weekly_questions;
    @Schema(description = "주별 인기 답변")
    private List<AnswerWeeklyResponseDto> popular_answers;
}
