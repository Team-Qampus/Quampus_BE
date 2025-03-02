package swyp.qampus.question.domain;

import lombok.Builder;
import lombok.Getter;
import swyp.qampus.answer.domain.AnswerResponseDto;
import swyp.qampus.image.domain.Image;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class QuestionDetailResponseDto {
    private final Long questionId;
    private final Long userId;
    private final String title;
    private final String content;
    private final String universityName;
    private final LocalDateTime createdDate;
    private final int viewCnt;
    private final int curiousCount;
    private final boolean isCurious;
    private final List<AnswerResponseDto> answers;
    private final List<String> imageUrls;

    @Builder
    private QuestionDetailResponseDto(Long questionId, Long userId, String title, String content, String universityName,
                                      LocalDateTime createdDate, int viewCnt, int curiousCount, boolean isCurious, List<AnswerResponseDto> answers,List<String> imageUrls) {

        this.questionId = questionId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.universityName = universityName;
        this.createdDate = createdDate;
        this.viewCnt = viewCnt;
        this.curiousCount = curiousCount;
        this.isCurious = isCurious;
        this.answers = answers;
        this.imageUrls = imageUrls;
    }


    public static QuestionDetailResponseDto of(Question question, boolean isCurious, List<AnswerResponseDto> answers,List<Image> images) {

        List<String> imageUrls = images.stream()
                .map(Image::getPictureUrl)
                .collect(Collectors.toList());

        return QuestionDetailResponseDto.builder()
                .questionId(question.getQuestionId())
                .userId(question.getUser().getUserId())
                .title(question.getTitle())
                .content(question.getContent())
                .universityName(question.getUser().getUniversity().getUniversityName())
                .createdDate(question.getCreateDate())
                .viewCnt(question.getViewCnt())
                .curiousCount(question.getCuriousCount())
                .isCurious(isCurious)
                .answers(answers)
                .imageUrls(imageUrls)
                .build();
    }
}
