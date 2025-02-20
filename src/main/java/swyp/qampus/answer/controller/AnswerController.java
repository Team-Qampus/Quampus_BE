package swyp.qampus.answer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.answer.domain.AnswerRequestDto;
import swyp.qampus.answer.domain.AnswerUpdateRequestDto;
import swyp.qampus.question.domain.MessageResponseDto;
import swyp.qampus.answer.domain.AnswerResponseDto;
import swyp.qampus.answer.service.AnswerService;

import java.util.List;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<AnswerResponseDto> createAnswer(@RequestPart(value = "requestDto",required = true) AnswerRequestDto requestDto,
                                                          @RequestPart(value = "images",required = false)List<MultipartFile>images) {
        return ResponseEntity.ok(answerService.createAnswer(requestDto,images));
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
