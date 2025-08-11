package swyp.qampus.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.like.service.LikeService;

@RestController
@RequestMapping("/like")
@Tag(name = "좋아요",description = "좋아요 API입니다.")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    @Operation(
            summary = "좋아요 API입니다.-[담당자 : 김도연]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "좋아요 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "중복된 좋아요 리소스 요청입니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "400",description = "답변이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }
    )
    @PostMapping
    public ResponseEntity<?> insertLike(
            @Parameter(description = "좋아요를 할 답변 ID")
            @RequestParam("answer")Long answerId,

            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader("Authorization")String token
    ){
        likeService.insert(token,answerId);
        return ResponseEntity.ok().body(ResponseDto.of(true,200,"좋아요 성공"));
    }
    @Operation(
            summary = "좋아요 취소 API입니다.-[담당자 : 김도연]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "좋아요 취소 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "중복된 좋아요 리소스 요청입니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class))),
                    @ApiResponse(responseCode = "400",description = "답변이 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }
    )
    @DeleteMapping
    public ResponseEntity<?> deleteLike(
            @Parameter(description = "좋아요를 취소할 답변 ID")
            @RequestParam("answer")Long answerId,

            @Parameter(description = "Bearer 토큰을 포함한 Authorization 헤더")
            @RequestHeader("Authorization")String token
    ){
        likeService.delete(token,answerId);
        return ResponseEntity.ok().body(ResponseDto.of(true,200,"좋아요 취소 성공"));
    }
}
