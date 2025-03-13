package swyp.qampus.university.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.university.domain.response.UniversityDetailResponseDto;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;
import swyp.qampus.university.service.UniversityService;

import java.util.*;

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
                                    array = @ArraySchema(schema = @Schema(implementation = UniversityRankResponseDto.class)),
                                    examples = @ExampleObject(value = """
                                        {
                                          "rank": [
                                            {
                                              "university_id": 1,
                                              "university_name": "대학1",
                                              "ranking": 1,
                                              "participant_count": 12000,
                                              "rate": 50,
                                              "choice_cnt": 123,
                                              "location":{
                                                "latitude":13.22,
                                                "longitude":1.123
                                              }
                                            },
                                            {
                                              "university_id": 2,
                                              "university_name": "대학2",
                                              "ranking": 2,
                                              "participant_count": 12000,
                                              "rate": 30,
                                              "choice_cnt": 123,
                                              "location":{
                                                "latitude":13.22,
                                                "longitude":1.123
                                              }
                                            },
                                            {
                                              "university_id": 13,
                                              "university_name": "대학3",
                                              "ranking": 3,
                                              "participant_count": 12000,
                                              "rate": 10,
                                              "choice_cnt": 123,
                                              "location":{
                                                "latitude":13.22,
                                                "longitude":1.123
                                              }
                                            }
                                          ]
                                        }
                                    """))
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorCode.class)))
            }
    )
    @GetMapping("/university/rank")
    public ResponseEntity<?>getRanking(

            @Parameter(description = "조회할 최대 학교 개수")
            @RequestParam(value = "limit",required = false)Integer limit,

            @Parameter(description = "조회할 기간 입력값:weekly->주간 monthly->월간")
            @RequestParam(value = "period")String period
    ){
        Optional<List<UniversityRankResponseDto>>results=universityService
                .getUniversityRanking(limit,period);

        HashMap<String, List<UniversityRankResponseDto>> response=new HashMap<>();
        response.put("ranking",results.orElse(null));

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "학교 상세보기 조회 API입니다.-[담당자 : 김도연]",
            description = "rate:0~100 퍼센트 변환입니다.",
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
    public ResponseEntity<?>getUniversityName(
            @Parameter(description = "대학교 이름")
            @RequestParam("universityName")String universityName
    ){
        UniversityDetailResponseDto universityDetail = universityService.getUniversityDetail(universityName);
        if(universityDetail==null){
            return ResponseEntity.ok(Collections.EMPTY_MAP);
        }
        return ResponseEntity.ok(universityDetail);
    }

    @PutMapping("/university/change")
    public ResponseEntity<?>changeUniversityName(@RequestParam("universityName")String universityName,@RequestParam("universityId")Long universityId){
        universityService.changeUniversityName(universityName,universityId);
        return ResponseEntity.ok("변경 성공");
    }
}
