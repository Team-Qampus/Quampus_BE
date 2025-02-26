package swyp.qampus.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.login.service.UserService;
import swyp.qampus.question.domain.MyQuestionResponseDto;


import java.util.List;
@Tag(name = "마이페이지",description = "마이페이지-내가 작성한 질문 조회API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Operation(
            summary = "마이페이지 API입니다.-[담당자 : 박재하]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MyQuestionResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "400",description = "질문이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }
    )
    @GetMapping("/questions/{category_id}")
    public ResponseEntity<List<MyQuestionResponseDto>> getMyQuestions(
            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader("Authorization")String token,

            @Parameter(description = "조회할 질문 카테고리 ID")
            @PathVariable(value = "category_id", required = false) Long categoryId,

            @Parameter(description = "조회할 정렬 방법")
            @RequestParam(value = "sort", defaultValue = "latest") String sort,
            Pageable pageable) {
        List<MyQuestionResponseDto> questions = userService.getMyQuestions(token, categoryId, sort, pageable);
        return ResponseEntity.ok(questions);
    }

}