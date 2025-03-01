package swyp.qampus.answer.domain;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceRequestDto {
    private Long question_id;
    private Long answer_id;
    private Boolean is_chosen;

}
