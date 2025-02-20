package swyp.qampus.answer.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ChoiceRequestDto {
    private Long question_id;
    private Long answer_id;
    private Boolean is_chosen;

}
