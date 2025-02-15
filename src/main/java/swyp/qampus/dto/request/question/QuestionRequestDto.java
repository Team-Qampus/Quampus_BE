package swyp.qampus.dto.request.question;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionRequestDto {
    private String title;
    private String content;
    private Long category_id;
    private List<String> images;
}
