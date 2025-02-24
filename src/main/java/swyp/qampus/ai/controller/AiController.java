package swyp.qampus.ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import swyp.qampus.ai.domain.response.AiResponseDto;
import swyp.qampus.ai.service.AiService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AiController {
    private final AiService aiService;

    @GetMapping("/{question_id}/ai")
    public ResponseEntity<AiResponseDto> getAiAnswer(@RequestHeader("Authorization")String token,
                                                     @PathVariable("question_id")Long question_id) throws IOException {
        return ResponseEntity.ok().body(aiService.getAiAnswer(token,question_id));
    }
}
