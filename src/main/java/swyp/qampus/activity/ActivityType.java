package swyp.qampus.activity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ActivityType {
    QUESTION("답변 등록"),
    ANSWER("질문 등록"),
    CHOICE_SAVE("채택 등록"),
    CHOICE_DELETE("채택 취소");

    private final String name;
}
