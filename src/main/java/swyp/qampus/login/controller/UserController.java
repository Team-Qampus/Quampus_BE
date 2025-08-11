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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.login.dto.MyPageResponseDto;
import swyp.qampus.login.service.UserService;


import java.net.URISyntaxException;
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
                                    array = @ArraySchema(schema = @Schema(implementation = MyPageResponseDto.class)))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "400", description = "질문이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }
    )
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponseDto> getMyPage(
            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader("Authorization")String token,

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
        MyPageResponseDto response = userService.getMyPageData(token, categoryId, sort, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "테스트용 프리패스 토큰 발급 API입니다. -[담당자 : 박재하]",
            description = "테스트 환경에서 인증 없이 API를 테스트할 수 있도록 프리패스 토큰을 발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "테스트용 프리패스 토큰 발급 성공",
                            content = @Content(
                                    examples = @ExampleObject(value = """
                                        eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbO2FjOyKpO2KuDJAbmF2ZXIuY29tIiwidXNlcklkIjo1NSwiaWF0IjoxNzQwODIyMDY1LCJleHAiOjE3NDA5MDg0NjV9.TP2EzzzhgAiy79O-NNY3125eCtwfPTImheo2izRRlsI
                                        """))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }
    )
    @PostMapping("/test/user")
    public ResponseEntity<?>createUser(@RequestParam("userName")String userName,
                                       @RequestParam("universityName")String universityName,
                                       @RequestParam("major")String major) throws URISyntaxException {
        return ResponseEntity.ok(userService.testUser(userName,universityName,major));
    }

}