package swyp.qampus.dto.request.question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionUpdateRequestDto {
    private String title;
    private String content;
    private Long category_id;
}
