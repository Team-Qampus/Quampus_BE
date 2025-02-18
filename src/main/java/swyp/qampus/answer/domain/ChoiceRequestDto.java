package swyp.qampus.answer.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ChoiceRequestDto {
    Long question_id;
    Long answer_id;
    Boolean is_chosen;

}
