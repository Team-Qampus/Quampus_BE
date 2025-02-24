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

    public AnswerRequestDto(String userId, Long questionId, String 테스트_답변) {
    }
}
