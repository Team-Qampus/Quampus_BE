package swyp.qampus.curious.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.curious.service.CuriousService;
import swyp.qampus.exception.ErrorCode;

@RestController
@RequestMapping("/curious")
@RequiredArgsConstructor
@Tag(name = "나도 궁금해요", description = "나도 궁금해요 API")
public class CuriousController {
    private final CuriousService curiousService;
    @Operation(
            summary = "나도 궁금해요 하기 API입니다.-[담당자 : 김도연]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "나도 궁금해요 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "중복된 나도 궁금해요 리소스 요청입니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "400", description = "자신의 질문에는 '나도 궁금해요'를 클릭할 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "400",description = "질문이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }
    )
    @PostMapping
    public ResponseEntity<?> insertCurious(@RequestParam("question")Long questionId,
                                           @RequestHeader("Authorization")String token){
        curiousService.insert(token, questionId);
        return ResponseEntity.ok().body(ResponseDto.of(true,200,"나도 궁금해요 성공"));
    }
    @Operation(
            summary = "나도 궁금해요 취소 API입니다.-[담당자 : 김도연]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "나도 궁금해요 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "중복된 나도 궁금해요 리소스 요청입니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "400", description = "자신의 질문에는 '나도 궁금해요'를 클릭할 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "400",description = "질문이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }
    )
    @DeleteMapping
    public ResponseEntity<?> deleteCurious(@RequestParam("question")Long questionId,
                                           @RequestHeader("Authorization")String token){
        curiousService.delete(token,questionId);
        return ResponseEntity.ok().body(ResponseDto.of(true,200,"나도 궁금해요 취소"));
    }
}
