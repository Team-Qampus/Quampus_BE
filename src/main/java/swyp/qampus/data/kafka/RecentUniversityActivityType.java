package swyp.qampus.data.kafka;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RecentUniversityActivityType {
    ANSWER("답변 작성"),
    QUESTION("질문 작성"),
    CHOICE("채택"),
    LIKE("좋아요"),
    CURIOUS("나도 궁금해요");

    private final String message;
}
