package swyp.qampus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.dto.request.answer.AnswerRequestDto;
import swyp.qampus.dto.request.answer.AnswerUpdateRequestDto;
import swyp.qampus.dto.response.MessageResponseDto;
import swyp.qampus.dto.response.answer.AnswerResponseDto;
import swyp.qampus.service.AnswerService;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<AnswerResponseDto> createAnswer(@RequestBody AnswerRequestDto requestDto) {
        return ResponseEntity.ok(answerService.createAnswer(requestDto));
    }

    @PutMapping("/{answer_id}")
    public ResponseEntity<MessageResponseDto> updateAnswer(@PathVariable Long answer_id,
                                                           @RequestBody AnswerUpdateRequestDto requestDto) {
        return ResponseEntity.ok(answerService.updateAnswer(answer_id, requestDto));
    }

    @DeleteMapping("/{answer_id}")
    public ResponseEntity<MessageResponseDto> deleteAnswer(@PathVariable Long answer_id) {
        return ResponseEntity.ok(answerService.deleteAnswer(answer_id));
    }
}
