package swyp.qampus.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swyp.qampus.question.domain.MyQuestionResponseDto;
import swyp.qampus.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/questions")
    public ResponseEntity<List<MyQuestionResponseDto>> getMyQuestions(
            @RequestParam Long userId,
            @RequestParam(value = "category_id", required = false) Long categoryId,
            @RequestParam(value = "sort", defaultValue = "latest") String sort,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        List<MyQuestionResponseDto> questions = userService.getMyQuestions(userId, categoryId, sort, page, size);
        return ResponseEntity.ok(questions);
    }

}