package swyp.qampus.answer.domain;

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

    public AnswerResponseDto(Answer answer) {
        this.answerId = answer.getAnswerId();
        this.userId = answer.getUser().getUserId();
        this.content = answer.getContent();
        this.createdDate = answer.getCreateDate();
        this.likeCount = answer.getLikeCnt();
        this.isChosen = answer.getIsChosen();
    }
}
