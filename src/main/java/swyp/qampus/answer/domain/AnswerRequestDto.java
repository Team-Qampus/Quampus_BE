package swyp.qampus.answer.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerRequestDto {
    private String user_id;
    private Long question_id;
    private String content;
}
