package swyp.qampus.question.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionResponseDto {
    private Long question_id;

    public QuestionResponseDto(Long question_id) {
        this.question_id = question_id;
    }
}
