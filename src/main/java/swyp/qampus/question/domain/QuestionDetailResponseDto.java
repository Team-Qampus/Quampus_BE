package swyp.qampus.question.domain;

import lombok.Getter;
import swyp.qampus.answer.domain.AnswerResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class QuestionDetailResponseDto {
    private final Long questionId;
    private final String title;
    private final String content;
    private final String universityName;
    private final LocalDateTime createdDate;
    private final int viewCnt;
    private final int curiousCount;
    private final List<AnswerResponseDto> answers;

    public QuestionDetailResponseDto(Question question, List<AnswerResponseDto> answers) {
        this.questionId = question.getQuestionId();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.universityName = question.getUser().getUniversityName();
        this.createdDate = question.getCreateDate();
        this.viewCnt = question.getViewCnt();
        this.curiousCount = question.getCuriousCount();
        this.answers = answers;
    }
}
