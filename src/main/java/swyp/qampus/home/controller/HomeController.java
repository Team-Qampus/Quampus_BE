package swyp.qampus.home.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swyp.qampus.home.dto.HomeResponseDto;
import swyp.qampus.home.service.HomeService;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @GetMapping
    public ResponseEntity<HomeResponseDto> getWeeklyPopularContent() {
        return ResponseEntity.ok(homeService.getWeeklyPopularQna());
    }
}