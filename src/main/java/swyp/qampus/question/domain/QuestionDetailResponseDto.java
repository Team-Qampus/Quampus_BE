package swyp.qampus.question.domain;

import lombok.Builder;
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

    @Builder
    private QuestionDetailResponseDto(Long questionId, String title, String content, String universityName,
                                      LocalDateTime createdDate, int viewCnt, int curiousCount, List<AnswerResponseDto> answers) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.universityName = universityName;
        this.createdDate = createdDate;
        this.viewCnt = viewCnt;
        this.curiousCount = curiousCount;
        this.answers = answers;
    }

    public static QuestionDetailResponseDto of(Question question, List<AnswerResponseDto> answers) {
        return QuestionDetailResponseDto.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .content(question.getContent())
                .universityName(question.getUser().getUniversity().getUniversityName())
                .createdDate(question.getCreateDate())
                .viewCnt(question.getViewCnt())
                .curiousCount(question.getCuriousCount())
                .answers(answers)
                .build();
    }
}
