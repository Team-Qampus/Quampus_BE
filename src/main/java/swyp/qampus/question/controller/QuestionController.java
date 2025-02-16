package swyp.qampus.question.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.question.domain.QuestionRequestDto;
import swyp.qampus.question.domain.QuestionUpdateRequestDto;
import swyp.qampus.question.domain.MessageResponseDto;
import swyp.qampus.answer.domain.QuestionResponseDto;
import swyp.qampus.question.service.QuestionService;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/{user_id}")
    public ResponseEntity<QuestionResponseDto> createQuestion(@PathVariable String user_id,
                                                              @RequestBody QuestionRequestDto requestDto) {
        return ResponseEntity.ok(questionService.createQuestion(user_id, requestDto));
    }

    @PutMapping("/{question_id}")
    public ResponseEntity<MessageResponseDto> updateQuestion(@PathVariable Long question_id,
                                                             @RequestBody QuestionUpdateRequestDto requestDto) {
        return ResponseEntity.ok(questionService.updateQuestion(question_id, requestDto));
    }

    @DeleteMapping("/{question_id}")
    public ResponseEntity<MessageResponseDto> deleteQuestion(@PathVariable Long question_id) {
        return ResponseEntity.ok(questionService.deleteQuestion(question_id));
    }
}
