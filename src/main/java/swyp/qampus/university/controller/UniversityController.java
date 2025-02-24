package swyp.qampus.university.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.university.domain.response.UniversityDetailResponseDto;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;
import swyp.qampus.university.service.UniversityService;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "커뮤니티",description = "학교 조회 API입니다.")
public class UniversityController {
    private final UniversityService universityService;

    @Operation(
            summary = "학교 랭킹 조회 API입니다.-[담당자 : 김도연]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UniversityRankResponseDto.class))),
            }
    )
    @GetMapping("/university/rank")
    public ResponseEntity<?>getRanking(@RequestHeader("Authorization")String token,
                                       @RequestParam(value = "limit",required = false)Integer limit,
                                       @RequestParam(value = "period")String period){
        Optional<List<UniversityRankResponseDto>>results=universityService
                .getUniversityRanking(token,limit,period);

        HashMap<String, List<UniversityRankResponseDto>> response=new HashMap<>();
        response.put("ranking",results.orElse(null));

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "학교 상세보기 조회 API입니다.-[담당자 : 김도연]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UniversityDetailResponseDto.class))),
                    @ApiResponse(responseCode = "404",description = "학교가 존재하지 않습니다.",
                            content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = ErrorCode.class)))
            }
    )
    @GetMapping("/university/detail")
    public ResponseEntity<?>getUniversityName(@RequestHeader("Authorization")String token,
                                              @RequestParam("name")String universityName){

        return ResponseEntity.ok(universityService.getUniversityDetail(token,universityName));
    }
}
