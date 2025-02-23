package swyp.qampus.answer.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerRequestDto {
    private Long user_id;
    private Long question_id;
    private String content;
}
