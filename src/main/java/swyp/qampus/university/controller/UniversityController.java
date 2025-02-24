package swyp.qampus.university.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;
import swyp.qampus.university.service.UniversityService;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UniversityController {
    private final UniversityService universityService;

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

    @GetMapping("/university/detail")
    public ResponseEntity<?>getUniversityName(@RequestHeader("Authorization")String token,
                                              @RequestParam("name")String universityName){

        return ResponseEntity.ok(universityService.getUniversityDetail(token,universityName));
    }
}
