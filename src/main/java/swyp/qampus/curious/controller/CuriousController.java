package swyp.qampus.curious.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.curious.service.CuriousService;

@RestController
@RequestMapping("/curious")
@RequiredArgsConstructor
public class CuriousController {
    private final CuriousService curiousService;

    @PostMapping
    public ResponseEntity<?> insertCurious(@RequestParam("question")Long questionId,
                                           @RequestHeader("Authorization")String token){
        curiousService.insert(token, questionId);
        return ResponseEntity.ok().body(ResponseDto.of(true,200,"나도 궁금해요 성공"));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCurious(@RequestParam("question")Long questionId,
                                           @RequestHeader("Authorization")String token){
        curiousService.delete(token,questionId);
        return ResponseEntity.ok().body(ResponseDto.of(true,200,"나도 궁금해요 취소"));
    }
}
