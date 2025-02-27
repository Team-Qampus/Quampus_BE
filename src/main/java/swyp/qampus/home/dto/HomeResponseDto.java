package swyp.qampus.home.dto;

import lombok.Builder;
import lombok.Getter;
import swyp.qampus.answer.domain.AnswerWeeklyResponseDto;
import swyp.qampus.question.domain.QuestionWeeklyResponseDto;

import java.util.List;

@Getter
@Builder
public class HomeResponseDto {
    private UserHomeDto userHomeDto;
    private List<QuestionWeeklyResponseDto> weeklyQuestions;
    private List<AnswerWeeklyResponseDto> weeklyAnswers;

    //회원
    public static HomeResponseDto of(UserHomeDto userHomeDto,
                                     List<QuestionWeeklyResponseDto> weeklyQuestions,
                                     List<AnswerWeeklyResponseDto> weeklyAnswers) {
        return HomeResponseDto.builder()
                .userHomeDto(userHomeDto)
                .weeklyQuestions(weeklyQuestions)
                .weeklyAnswers(weeklyAnswers)
                .build();
    }

    //비회원
    public static HomeResponseDto fromQuestions(List<QuestionWeeklyResponseDto> weeklyQuestions) {
        return HomeResponseDto.builder()
                .userHomeDto(null)
                .weeklyQuestions(weeklyQuestions)
                .weeklyAnswers(List.of())
                .build();
    }

    public static HomeResponseDto withoutUser(List<QuestionWeeklyResponseDto> weeklyQuestions,
                                              List<AnswerWeeklyResponseDto> weeklyAnswers) {
        return HomeResponseDto.builder()
                .userHomeDto(null)
                .weeklyQuestions(weeklyQuestions)
                .weeklyAnswers(weeklyAnswers)
                .build();
    }
}