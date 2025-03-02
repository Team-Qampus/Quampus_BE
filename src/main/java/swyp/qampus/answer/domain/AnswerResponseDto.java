package swyp.qampus.answer.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AnswerResponseDto {
    private final Long answerId;
    private final Long userId;
    private final String content;
    private final LocalDateTime createdDate;
    private final int likeCount;
    private final boolean isChosen;
    private String universityName;

    @Builder
    public AnswerResponseDto(Long answerId, Long userId, String content,
                             LocalDateTime createdDate, int likeCount, boolean isChosen, String universityName) {
        this.answerId = answerId;
        this.userId = userId;
        this.content = content;
        this.createdDate = createdDate;
        this.likeCount = likeCount;
        this.isChosen = isChosen;
        this.universityName = universityName;
    }

    public static AnswerResponseDto of(Answer answer) {
        return new AnswerResponseDto(
                answer.getAnswerId(),
                answer.getUser().getUserId(),
                answer.getContent(),
                answer.getCreateDate(),
                answer.getLikeCnt(),
                answer.getIsChosen(),
                answer.getUser().getUniversity().getUniversityName()
        );
    }
}
