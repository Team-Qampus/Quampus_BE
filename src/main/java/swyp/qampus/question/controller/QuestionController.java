package swyp.qampus.question.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.question.domain.QuestionRequestDto;
import swyp.qampus.question.domain.QuestionUpdateRequestDto;
import swyp.qampus.question.service.QuestionServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Schema(name = "질문",description = "질문하기 API입니다.")
public class QuestionController {
    private final QuestionServiceImpl questionService;

    @PostMapping("/{user_id}")
    public ResponseEntity<?> createQuestion(@PathVariable Long user_id,
                                                              @RequestPart(value = "requestDto", required = true) QuestionRequestDto requestDto,
                                                              @RequestPart(value = "images",required = false) List<MultipartFile> images) {
        questionService.createQuestion(user_id, requestDto, images);
        return ResponseEntity.ok(ResponseDto.of(true, 200, "질문 생성 성공"));
    }

    @PutMapping("/{question_id}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long question_id,
                                                             @RequestBody QuestionUpdateRequestDto requestDto) {
        questionService.updateQuestion(question_id, requestDto);
        return ResponseEntity.ok(ResponseDto.of(true, 200, "질문 수정 성공"));
    }

    @DeleteMapping("/{question_id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long question_id) {
        questionService.deleteQuestion(question_id);
        return ResponseEntity.ok(ResponseDto.of(true, 200, "질문 삭제 성공"));
    }
}
