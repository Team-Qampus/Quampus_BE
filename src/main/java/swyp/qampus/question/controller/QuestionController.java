package swyp.qampus.question.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import swyp.qampus.question.domain.CreateQuestionResponseDto;
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
            description = "카테고리 ID - 1:전체\n 2:자연계\n 3:인문계\n 4:예체능\n 5:실무\n",
            responses = {
                    @ApiResponse(responseCode = "200", description = "질문 생성 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CreateQuestionResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
            }
    )
    @PostMapping
    public ResponseEntity<?> createQuestion(
            @RequestPart(value = "requestDto") QuestionRequestDto requestDto,
            @RequestPart(value = "images",required = false) List<MultipartFile> images,

            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader("Authorization")String token
    ) {
        Long questionId = questionService.createQuestion(requestDto, images,token);
        return ResponseEntity.ok(CreateQuestionResponseDto.of(true, 200, "질문 생성 성공", questionId));
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
    public ResponseEntity<?> updateQuestion(
            @Parameter(description = "수정할 질문 ID")
            @PathVariable(name = "question_id") Long question_id,

            @RequestPart(value = "requestDto") QuestionUpdateRequestDto requestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,

            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader("Authorization")String token
    ) {
        questionService.updateQuestion(question_id, requestDto, images, token);
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
    public ResponseEntity<?> deleteQuestion(
            @Parameter(description = "삭제할 답변 ID")
            @PathVariable(name = "question_id") Long question_id,

            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader("Authorization")String token
    ) {
        questionService.deleteQuestion(question_id,token);
        return ResponseEntity.ok(ResponseDto.of(true, 200, "질문 삭제 성공"));
    }
}
