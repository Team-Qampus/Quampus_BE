package swyp.qampus.answer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.answer.domain.AnswerRequestDto;
import swyp.qampus.answer.domain.AnswerUpdateRequestDto;
import swyp.qampus.answer.domain.ChoiceRequestDto;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.answer.service.AnswerService;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.question.domain.QuestionDetailResponseDto;
import swyp.qampus.question.domain.QuestionListResponseDto;
import swyp.qampus.question.domain.QuestionResponseDto;

import java.util.List;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
@Tag(name = "답변", description = "답변 API")
public class AnswerController {
    private final AnswerService answerService;

    @Operation(
            summary = "답변 생성 API입니다.-[담당자:박재하],이미지 업로드 관련 - [담당자:김도연]",
            description = "form-data형식입니다. 이미지는 삽입안하셔도 됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "답변 생성 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "질문이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }
    )
    @PostMapping
    public ResponseEntity<?> createAnswer(
            @RequestPart(value = "requestDto") AnswerRequestDto requestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,

            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader("Authorization")String token
    ) {
        answerService.createAnswer(requestDto, images,token);
        return ResponseEntity.ok().body(ResponseDto.of(true, 200, "답변 생성 성공"));
    }

    @Operation(
            summary = "답변 수정 API입니다.-[담당자:박재하]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "답변 생성 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "답변이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }

    )
    @PutMapping("/{answer_id}")
    public ResponseEntity<?> updateAnswer(
            @Parameter(description = "수정할 답변 ID")
            @PathVariable(name = "answer_id") Long answer_id,

            @RequestBody AnswerUpdateRequestDto requestDto,

            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader("Authorization")String token
    ) {
        answerService.updateAnswer(answer_id, requestDto,token);
        return ResponseEntity.ok(ResponseDto.of(true, 200, "답변 수정 성공"));
    }

    @Operation(
            summary = "답변 삭제 API입니다.-[담당자:박재하]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "답변 삭제 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "답변이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }

    )
    @DeleteMapping("/{answer_id}")
    public ResponseEntity<?> deleteAnswer(
            @Parameter(description = "삭제할 답변 ID")
            @PathVariable(name = "answer_id") Long answer_id,

            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader("Authorization")String token
    ) {
        answerService.deleteAnswer(answer_id,token);
        return ResponseEntity.ok(ResponseDto.of(true, 200, "답변 삭제 성공"));
    }

    @Operation(
            summary = "채택하기 및 채택취소 API입니다.-[담당자:김도연]",
            description = "is_chosen값을 true로 전달하면 채택하기 / is_chosen값을 false로 전달하면 채택취소입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "채택 또는 취소 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "답변이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "질문이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "403", description = "권한이 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "400", description = "이 질문에는 이미 채택된 답변이 있습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "409", description = "이미 채택된 답변입니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "학교가 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }

    )
    @PutMapping("/choice")
    public ResponseEntity<?> choice(
            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader("Authorization") String token,

            @RequestBody ChoiceRequestDto requestDto
    ) {
        answerService.choice(requestDto, token);
        return ResponseEntity.ok().body(ResponseDto.of(true, 200, "채택 또는 취소 성공"));
    }

    @Operation(
            summary = "질문 리스트 API입니다.-[담당자:박재하]",
            description = "기본 sort=최신 게시물 순서입니다. size:한 페이지당 최대 항목 수 page:조회할 페이지 번호(0부터 시작)",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuestionListResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "질문이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }

    )
    @GetMapping
    public ResponseEntity<List<QuestionListResponseDto>> getQuestions(
            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader("Authorization") String token,

            @Parameter(description = "조회할 질문 카테고리 ID")
            @RequestParam(name = "category_id", defaultValue = "1", required = false) Long categoryId,

            @Parameter(description = "조회할 정렬 방법")
            @RequestParam(name = "sort", defaultValue = "latest") String sort,

            @Parameter(description = "조회할 페이지 번호 (0부터 시작)")
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,

            @Parameter(description = "한 페이지당 조회할 항목 수")
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(answerService.getQuestions(categoryId, sort, pageable,token));
    }

    @Operation(
            summary = "질문 상세보기 API입니다.-[담당자:박재하]",
            description = "질문 상세보기 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "채택 또는 취소 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuestionDetailResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "질문이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }

    )
    @GetMapping("/detail/{question_id}")
    public ResponseEntity<QuestionDetailResponseDto> getQuestionDetail(
            @Parameter(description = "상세한 정보를 조회할 질문 ID")
            @PathVariable(value = "question_id") Long question_id,

            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(answerService.getQuestionDetail(question_id, token));
    }

    @Operation(
            summary = "질문 검색하기 API입니다.-[담당자:박재하]",
            description = "질문 검색하기 입니다. 기본 sort=최신 게시물 순서입니다. size:한 페이지당 최대 항목 수 page:조회할 페이지 번호(0부터 시작)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "채택 또는 취소 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = QuestionResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "질문이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }

    )
    @GetMapping("/search")
    public ResponseEntity<List<QuestionResponseDto>> searchQuestions(
            @Parameter(description = "검색한 값")
            @RequestParam String value,

            @Parameter(description = "조회할 정렬 방법")
            @RequestParam(defaultValue = "latest") String sort,

            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader("Authorization") String token,

            @Parameter(description = "조회할 페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0", required = false) int page,

            @Parameter(description = "한 페이지당 조회할 항목 수")
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<QuestionResponseDto> questions = answerService.searchQuestions(value, sort, pageable,token);
        return ResponseEntity.ok(questions);
    }
}
