package swyp.qampus.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    @GetMapping("/questions")
    public ResponseEntity<List<MyQuestionResponseDto>> getMyQuestions(
            @RequestParam Long userId,
            @RequestParam(value = "category_id", required = false) Long categoryId,
            @RequestParam(value = "sort", defaultValue = "latest") String sort,
            Pageable pageable) {

        List<MyQuestionResponseDto> questions = userService.getMyQuestions(userId, categoryId, sort, pageable);
        return ResponseEntity.ok(questions);
    }

}