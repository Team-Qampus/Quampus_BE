package swyp.qampus.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import swyp.qampus.answer.domain.AnswerWeeklyResponseDto;
import swyp.qampus.question.domain.QuestionWeeklyResponseDto;

import java.util.List;

@Getter
@AllArgsConstructor
public class HomeResponseDto {
    private List<QuestionWeeklyResponseDto> weekly_questions;
    private List<AnswerWeeklyResponseDto> popular_answers;
}
