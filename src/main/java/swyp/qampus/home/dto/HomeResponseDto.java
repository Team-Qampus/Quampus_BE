package swyp.qampus.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import swyp.qampus.answer.domain.AnswerWeeklyResponseDto;
import swyp.qampus.question.domain.QuestionWeeklyResponseDto;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(name = "홈화면 인기 질문/답변 Dto")
public class HomeResponseDto {
    private List<QuestionWeeklyResponseDto> weekly_questions;
    private List<AnswerWeeklyResponseDto> popular_answers;
}
