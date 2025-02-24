package swyp.qampus.home.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.home.dto.HomeResponseDto;
import swyp.qampus.home.service.HomeService;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
@Tag(name = "홈화면", description = "홈화면 API")
public class HomeController {
    private final HomeService homeService;
    @Operation(
            summary = "홈화면 API입니다.-[담당자 : 박재하]",
            description = "매주 월요일 00:00에 데이터 초기화",
            responses = {
                    @ApiResponse(responseCode = "200", description = "나도 궁금해요 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HomeResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "금주의 인기 질문 또는 답변을 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }
    )
    @GetMapping
    public ResponseEntity<HomeResponseDto> getWeeklyPopularContent() {
        return ResponseEntity.ok(homeService.getWeeklyPopularQna());
    }
}