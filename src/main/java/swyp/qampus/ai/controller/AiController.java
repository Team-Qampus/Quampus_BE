package swyp.qampus.ai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import swyp.qampus.ai.domain.response.AiResponseDto;
import swyp.qampus.ai.service.AiService;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.exception.ErrorCode;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "AI답변",description = "AI 답변 API")
public class AiController {
    private final AiService aiService;
    @Operation(
            summary = "GPT AI 답변 API입니다.-[담당자:김도연]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AiResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "질문이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "500",description = "서버 내부 오류",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }
    )

    @GetMapping("/{question_id}/ai")
    public ResponseEntity<AiResponseDto> getAiAnswer(@RequestHeader("Authorization")String token,
                                                     @PathVariable("question_id")Long question_id) throws IOException {
        return ResponseEntity.ok().body(aiService.getAiAnswer(token,question_id));
    }
}
