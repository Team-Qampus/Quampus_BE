package swyp.qampus.question.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyQuestionResponseDto {
    private final Long questionId;
    private final String title;
    private final String content;
    private final String category;
    private final int viewCount;
    private final int answerCnt;
    private final int unreadAnswerCnt;
    private final LocalDateTime createdDate;

    public MyQuestionResponseDto(Question question) {
        this.questionId = question.getQuestionId();
        this.title = question.getTitle();
        this.category = question.getCategory().getCategoryName();
        this.content = question.getContent();
        this.createdDate = question.getCreateDate();
        this.viewCount = question.getViewCnt();
        this.answerCnt = question.getAnswerCount();
        this.unreadAnswerCnt = question.getUnreadAnswerCnt();
    }
}