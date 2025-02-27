package swyp.qampus.home.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.home.dto.HomeResponseDto;
import swyp.qampus.home.exception.HomeErrorCode;
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
                    @ApiResponse(responseCode = "200", description = "성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HomeResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "userHomeDto": {
                                                "name": "김하나",
                                                "universityName": "홍익대학교",
                                                "major": "시각디자인학과"
                                              },
                                              "weekly_questions": [
                                                {
                                                  "question_id": 1,
                                                  "title": "전공과 관련된 질문입니다.",
                                                  "university_name": "서울대학교",
                                                  "major": "컴퓨터공학과",
                                                  "curious_count": 10,
                                                  "view_count": 100,
                                                  "total_score": 80
                                                },
                                                {
                                                  "question_id": 2,
                                                  "title": "이공계 대학원 진학 고민입니다.",
                                                  "university_name": "KAIST",
                                                  "major": "기계공학과",
                                                  "curious_count": 8,
                                                  "view_count": 90,
                                                  "total_score": 70
                                                }
                                              ],
                                              "popular_answers": [
                                                {
                                                  "question_id": 1,
                                                  "answer_id": 10,
                                                  "title": "이 질문의 제목입니다.",
                                                  "university_name": "서울대학교",
                                                  "content": "이 질문에 대한 답변입니다.",
                                                  "like_count": 25
                                                },
                                                {
                                                  "question_id": 3,
                                                  "answer_id": 15,
                                                  "title": "대학원 입학 시 중요한 점은?",
                                                  "university_name": "연세대학교",
                                                  "content": "대학원에서는 연구 주제가 중요합니다.",
                                                  "like_count": 30
                                                }
                                              ]
                                            }
                                            """)
                            )),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "금주의 인기 질문 또는 답변을 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/jason",
                                    schema = @Schema(implementation = HomeErrorCode.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<HomeResponseDto> getHomeContent(
            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        if (token != null && !token.isEmpty()) {
            return ResponseEntity.ok(homeService.getUserHomeContent(token));
        } else {
            return ResponseEntity.ok(homeService.getGuestHomeContent());
        }
    }
}
