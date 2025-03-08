package swyp.qampus.activity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.activity.service.ActivityService;

@RestController
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;


    @GetMapping("/activity/recent")
    public ResponseEntity<?>recentActivities(@RequestHeader("Authorization")String token,
                                             @RequestParam("universityName")String universityName){

        return ResponseEntity.ok(activityService.getRecentActivities(token,universityName));
    }

}
