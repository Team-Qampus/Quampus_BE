package swyp.qampus.answer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.answer.domain.AnswerRequestDto;
import swyp.qampus.answer.domain.AnswerUpdateRequestDto;
import swyp.qampus.answer.domain.ChoiceRequestDto;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.question.domain.MessageResponseDto;
import swyp.qampus.answer.domain.AnswerResponseDto;
import swyp.qampus.answer.service.AnswerService;

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

    @PostMapping("/choice")
    public ResponseEntity<?> choice(@RequestHeader("Authorization")String token, @RequestBody ChoiceRequestDto requestDto){
        answerService.choice(requestDto,token);
        return ResponseEntity.ok().body(ResponseDto.of(true,200,"채택 성공"));
    }
}
