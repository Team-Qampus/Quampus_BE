package swyp.qampus.question.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.question.domain.QuestionRequestDto;
import swyp.qampus.question.domain.QuestionUpdateRequestDto;
import swyp.qampus.question.service.QuestionService;
import swyp.qampus.question.service.QuestionServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Tag(name = "질문",description = "질문하기 API입니다.")
public class QuestionController {
    private final QuestionService questionService;
    @Operation(
            summary = "질문하기 작성 API입니다.-[담당자 : 박재하]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "질문 생성 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
            }
    )
    @PostMapping("/{user_id}")
    public ResponseEntity<?> createQuestion(@PathVariable Long user_id,
                                                              @RequestPart(value = "requestDto", required = true) QuestionRequestDto requestDto,
                                                              @RequestPart(value = "images",required = false) List<MultipartFile> images) {
        questionService.createQuestion(user_id, requestDto, images);
        return ResponseEntity.ok(ResponseDto.of(true, 200, "질문 생성 성공"));
    }

    @Operation(
            summary = "질문하기 수정 API입니다.-[담당자 : 박재하]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "질문 수정 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "질문이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
            }
    )
    @PutMapping("/{question_id}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long question_id,
                                                             @RequestBody QuestionUpdateRequestDto requestDto) {
        questionService.updateQuestion(question_id, requestDto);
        return ResponseEntity.ok(ResponseDto.of(true, 200, "질문 수정 성공"));
    }

    @Operation(
            summary = "질문하기 삭제 API입니다.-[담당자 : 박재하]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "질문 삭제 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "질문이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
            }
    )
    @DeleteMapping("/{question_id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long question_id) {
        questionService.deleteQuestion(question_id);
        return ResponseEntity.ok(ResponseDto.of(true, 200, "질문 삭제 성공"));
    }
}
