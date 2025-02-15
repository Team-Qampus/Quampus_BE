package swyp.qampus.dto.response.answer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerResponseDto {
    private Long answer_id;
    private String message;

    public AnswerResponseDto(Long answer_id, String message) {
        this.answer_id = answer_id;
        this.message = message;
    }
}
