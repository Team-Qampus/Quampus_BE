package swyp.qampus.answer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.question.domain.QuestionDetailResponseDto;
import swyp.qampus.question.domain.QuestionListResponseDto;
import swyp.qampus.answer.domain.AnswerRequestDto;
import swyp.qampus.answer.domain.AnswerUpdateRequestDto;
import swyp.qampus.answer.domain.ChoiceRequestDto;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.answer.service.AnswerService;
import swyp.qampus.question.domain.QuestionResponseDto;

import java.util.List;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<?> createAnswer(@RequestPart(value = "requestDto", required = true) AnswerRequestDto requestDto,
                                          @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        answerService.createAnswer(requestDto, images);
        return ResponseEntity.ok().body(ResponseDto.of(true, 200, "답변 생성 성공"));
    }

    @PutMapping("/{answer_id}")
    public ResponseEntity<?> updateAnswer(@PathVariable(name = "answer_id") Long answer_id,
                                          @RequestBody AnswerUpdateRequestDto requestDto) {
        answerService.updateAnswer(answer_id, requestDto);
        return ResponseEntity.ok(ResponseDto.of(true, 200, "답변 수정 성공"));
    }

    @DeleteMapping("/{answer_id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable(name = "answer_id") Long answer_id) {
        answerService.deleteAnswer(answer_id);
        return ResponseEntity.ok(ResponseDto.of(true, 200, "답변 삭제 성공"));
    }

    @PostMapping("/choice")
    public ResponseEntity<?> choice(@RequestHeader("Authorization") String token, @RequestBody ChoiceRequestDto requestDto) {
        answerService.choice(requestDto, token);
        return ResponseEntity.ok().body(ResponseDto.of(true, 200, "채택 성공"));
    }

    @GetMapping
    public ResponseEntity<List<QuestionListResponseDto>> getQuestions(
            @PathVariable(value = "category_id") Long categoryId,
            @RequestParam(defaultValue = "latest") String sort,
            Pageable pageable) {

        return ResponseEntity.ok(answerService.getQuestions(categoryId, sort, pageable));
    }

    @GetMapping("/{question_id}")
    public ResponseEntity<QuestionDetailResponseDto> getQuestionDetail(@PathVariable Long question_id) {
        return ResponseEntity.ok(answerService.getQuestionDetail(question_id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<QuestionResponseDto>> searchQuestions(
            @RequestParam String value,
            @RequestParam(defaultValue = "latest") String sort,
            Pageable pageable) {

        List<QuestionResponseDto> questions = answerService.searchQuestions(value, sort, pageable);
        return ResponseEntity.ok(questions);
    }
}