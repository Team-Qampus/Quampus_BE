package swyp.qampus.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.login.dto.TokenResponseDto;
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
                                    array = @ArraySchema(schema = @Schema(implementation = MyQuestionResponseDto.class)))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "400", description = "질문이 존재하지 않습니다.",
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
    @Operation(
            summary = "테스트용 프리패스 토큰 발급 API입니다. -[담당자 : 박재하]",
            description = "테스트 환경에서 인증 없이 API를 테스트할 수 있도록 프리패스 토큰을 발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "테스트용 프리패스 토큰 발급 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TokenResponseDto.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "success": true,
                                          "code": 200,
                                          "message": "테스트용 프리패스 토큰 발급 성공",
                                          "token": "eyJhbGciOiJIUzI1NiJ9..."
                                        }
                                        """))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }
    )
    @PostMapping("/test/user")
    public ResponseEntity<?>createUser(@RequestParam("userName")String userName,
                                       @RequestParam("universityName")String universityName,
                                       @RequestParam("major")String major){
        return ResponseEntity.ok(userService.testUser(userName,universityName,major));
    }

}