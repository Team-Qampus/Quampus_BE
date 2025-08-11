package swyp.qampus.activity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.activity.dto.RecentActivityResponseDto;
import swyp.qampus.activity.service.ActivityService;

@RestController
@RequiredArgsConstructor
@Tag(name = "대학별 최신 활동",description = "최신 활동 API")
public class ActivityController {
    private final ActivityService activityService;

    @Operation(
            summary = "대학별 최신 활동 5개 조회 API입니다.-[담당자:김도연]",
            description = "대학 이름을 Param값으로 받아 조회합니다. 존재하지 않는 대학인 경우 예외처리를 합니다. 혹시 빈 배열로 반환을 원하시면 수정하겠습니다." +
                    "하단에 반환된 JSON에 대한 상세 설명 있습니다. -> 학교별 최신 활동 5개 조회Dto"

    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대학별 최신 활동 5개 조회 성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RecentActivityResponseDto.class))),
    })
    @GetMapping("/activity/recent")
    public ResponseEntity<?>recentActivities(@RequestParam("universityName")String universityName){

        return ResponseEntity.ok(activityService.getRecentActivities(universityName));
    }

}
